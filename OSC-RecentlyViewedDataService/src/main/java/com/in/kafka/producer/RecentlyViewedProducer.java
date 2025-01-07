package com.in.kafka.producer;

import com.in.configs.AppConstants;
import com.in.kafka.avro.RecentlyViewedHistory;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class RecentlyViewedProducer {
    private final KafkaTemplate<String, Object> kafkaTemplate;
    public void publishRecentViewedProducts(String userId, RecentlyViewedHistory recentlyViewedHistory){
        kafkaTemplate.send(AppConstants.RECENTLY_VIEWED_HISTORY_TOPIC, userId, recentlyViewedHistory);
    }
}
