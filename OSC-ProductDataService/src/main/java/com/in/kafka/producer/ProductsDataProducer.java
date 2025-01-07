package com.in.kafka.producer;

import com.in.configs.AppConstants;
import com.in.kafka.avro.product.ProductDetails;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class ProductsDataProducer {
    private final KafkaTemplate<String, Object> kafkaTemplate;
    public void publishProductsData(String productId, ProductDetails productDetails){
        kafkaTemplate.send(AppConstants.PRODUCT_DETAILS_TOPIC,productId, productDetails);
    }
}
