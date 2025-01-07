package com.in.services.serviceimpls;

import com.in.entities.RecentlyViewedEntity;
import com.in.kafka.avro.RecentlyViewedHistory;
import com.in.repositories.RecentlyViewedRepository;
import lombok.RequiredArgsConstructor;
import org.apache.kafka.streams.KeyValue;
import org.apache.kafka.streams.state.KeyValueIterator;
import org.apache.kafka.streams.state.ReadOnlyKeyValueStore;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
@RequiredArgsConstructor
public class ScheduleService {

    private static final Logger logger = LogManager.getLogger(ScheduleService.class);
    private final ReadOnlyKeyValueStore<String, RecentlyViewedHistory> recentlyViewedStore;
    private final RecentlyViewedRepository recentlyViewedRepository;
    /**
     * Scheduled task will run every 60 seconds (60000 milliseconds).
     * It fetches the recently viewed products data from the Kafka store
     * and persists it to the database using the recentlyViewedRepository.
     */
    @Scheduled(fixedRate = 60000)
    private void saveRecentlyViewedProductsToDB() {
        logger.info("Starting scheduled task: Saving recently viewed products to the database.");

        KeyValueIterator<String, RecentlyViewedHistory> recentlyViewedList = recentlyViewedStore.all();
        List<RecentlyViewedEntity> recentlyViewedEntities = new ArrayList<>();

        while (recentlyViewedList.hasNext()) {
            KeyValue<String, RecentlyViewedHistory> keyValue = recentlyViewedList.next();
            String userId = keyValue.key;
            RecentlyViewedHistory value = keyValue.value;

            logger.debug("Processing userId: {} with productIds: {}", userId, value.getProductIds());

            RecentlyViewedEntity entity = new RecentlyViewedEntity(userId, value.getProductIds());
            recentlyViewedEntities.add(entity);
        }

        if (recentlyViewedEntities.isEmpty()) {
            logger.warn("No recently viewed products found in the store to save.");
        } else {
            logger.info("Saving recently viewed product entries to the database.");
            recentlyViewedRepository.saveAll(recentlyViewedEntities);
            logger.info("Successfully saved recently viewed products to the database.");
        }

        logger.info("Completed scheduled task: Saving recently viewed products to the database.");
    }
}
