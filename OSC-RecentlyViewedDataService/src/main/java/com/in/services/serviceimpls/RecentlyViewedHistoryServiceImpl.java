package com.in.services.serviceimpls;

import com.in.dtos.RecentlyViewedProductDTO;
import com.in.dtos.UpdateRecentlyViewedDTO;
import com.in.entities.RecentlyViewedEntity;
import com.in.kafka.avro.RecentlyViewedHistory;
import com.in.kafka.producer.RecentlyViewedProducer;
import com.in.mappers.DataMapper;
import com.in.repositories.RecentlyViewedRepository;
import com.in.services.RecentlyViewedHistoryService;
import lombok.RequiredArgsConstructor;
import org.apache.kafka.streams.state.ReadOnlyKeyValueStore;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class RecentlyViewedHistoryServiceImpl implements RecentlyViewedHistoryService {

    private final Logger log = LogManager.getLogger(RecentlyViewedHistoryServiceImpl.class);
    private final RecentlyViewedProducer recentlyViewedProducer;
    private final ReadOnlyKeyValueStore<String, RecentlyViewedHistory> recentlyViewedStore;
    private final RecentlyViewedRepository recentlyViewedRepository;

    @Override
    public Boolean updateRecentlyViewedProducts(UpdateRecentlyViewedDTO dto) {
        log.info("Attempting to update recently viewed products for userId: {}", dto.getUserId());

        RecentlyViewedHistory initialHistory = recentlyViewedStore.get(dto.getUserId());
        List<String> productIdsList;

        if (initialHistory != null) {
            log.info("Existing recently viewed products for userId: {} are: {}", dto.getUserId(), initialHistory.getProductIds());
            productIdsList = new ArrayList<>(initialHistory.getProductIds());
        } else {
            log.warn("No recently viewed products found for userId: {}", dto.getUserId());
            productIdsList = new ArrayList<>();
        }

        productIdsList = productIdsList.stream()
                .filter(productId -> !productId.trim().equals(dto.getProductId()))
                .collect(Collectors.toList());

        productIdsList.add(0, dto.getProductId());

        if (productIdsList.size() > 6) {
            log.info("Getting recently viewed products list to the most recent 6 for userId: {}", dto.getUserId());
            productIdsList = productIdsList.subList(0, 6);
        }

        log.debug("Final updated product list for userId {}: {}", dto.getUserId(), productIdsList);

        RecentlyViewedHistory updatedViewProduct = DataMapper.dtoToAvro(productIdsList);
        recentlyViewedProducer.publishRecentViewedProducts(dto.getUserId(), updatedViewProduct);

        log.info("Successfully updated and published recently viewed products for userId: {}", dto.getUserId());
        return true;
    }

    @Override
    public List<String> fetchRecentlyViewedProducts(String userId) {
        log.info("Fetching recently viewed products for userId: {}", userId);

        RecentlyViewedHistory userProductsHistory = recentlyViewedStore.get(userId);
        if (userProductsHistory == null) {
            log.warn("No recently viewed product found for userId: {}", userId);
            return new ArrayList<>();
        }

        List<String> productIds = userProductsHistory.getProductIds().stream().toList();
        log.info("Found recently viewed products for userId {}: {}", userId, productIds);
        return productIds;
    }

    @Override
    public void saveRecentlyViewedProducts(RecentlyViewedProductDTO dto) {
        log.info("Saving recently viewed products for userId: {}", dto.getUserId());
        RecentlyViewedEntity mappedEntity = DataMapper.dtoToEntity(dto);
        log.debug("Mapped DTO to Entity: {}", mappedEntity);

        recentlyViewedRepository.save(mappedEntity);
        log.info("Successfully saved recently viewed products for userId: {}", dto.getUserId());
    }
}
