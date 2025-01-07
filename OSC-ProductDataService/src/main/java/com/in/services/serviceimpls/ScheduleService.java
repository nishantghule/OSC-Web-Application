package com.in.services.serviceimpls;

import com.in.entities.ViewCountEntity;
import com.in.kafka.avro.product.ProductViewCount;
import com.in.repositories.ProductViewCountRepository;
import lombok.RequiredArgsConstructor;
import org.apache.kafka.streams.KeyValue;
import org.apache.kafka.streams.state.KeyValueIterator;
import org.apache.kafka.streams.state.ReadOnlyKeyValueStore;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.scheduling.annotation.Scheduled;

@RequiredArgsConstructor
public class ScheduleService {
    private static final Logger logger = LogManager.getLogger(ScheduleService.class);
    private final ProductViewCountRepository productViewCountRepository;
    private final ReadOnlyKeyValueStore<String, ProductViewCount> productViewCountStore;
    /**
     * Scheduled task runs every 60 seconds (60000 milliseconds).
     * It fetches the current product view count data from the Kafka store
     * and persists it to the database using the productViewCountRepository.
     */
    @Scheduled(fixedRate = 60000)
    private void saveProductsViewCountToDB() {
        logger.info("Scheduled task to save product view count to DB started.");

        KeyValueIterator<String, ProductViewCount> viewCountList = productViewCountStore.all();
        while (viewCountList.hasNext()) {
            KeyValue<String, ProductViewCount> next = viewCountList.next();
            String productId = next.key;
            ProductViewCount productViewCount = next.value;
            String catId = productViewCount.getCategoryId().toString();
            int totalViewCount = productViewCount.getViewCount();
            ViewCountEntity entity = new ViewCountEntity(productId, catId, totalViewCount);

            logger.debug("Saving product ID: {} (Category ID: {}) with view count: {} to DB.", productId, catId, totalViewCount);

            try {
                productViewCountRepository.save(entity);
                logger.debug("Successfully saved product ID: {} to DB.", productId);
            } catch (Exception e) {
                logger.error("Failed to save product ID: {} (Category ID: {}) to DB. Error: {}", productId, catId, e.getMessage(), e);
            }
        }

        logger.info("Scheduled task to save product view counts to DB completed.");
    }
}
