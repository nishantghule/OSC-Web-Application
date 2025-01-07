package com.in.kafka.producer;

import com.in.configs.AppConstants;
import com.in.kafka.avro.product.ProductViewCount;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class ProductViewCountProducer {
    private final KafkaTemplate<String, Object> kafkaTemplate;
    public void publishProductViewCount(String productId, ProductViewCount productViewCount){
        kafkaTemplate.send(AppConstants.PRODUCT_VIEW_COUNT_TOPIC, productId, productViewCount);
    }
}
