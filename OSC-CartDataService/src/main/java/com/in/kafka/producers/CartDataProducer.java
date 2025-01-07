package com.in.kafka.producers;

import com.in.configs.AppConstants;
import com.in.kafka.avro.cart.CartData;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class CartDataProducer {
    private final KafkaTemplate<String, Object> kafkaTemplate;
    public void publishCartProductData(String userId, CartData cartData){
        kafkaTemplate.send(AppConstants.CART_DETAILS_TOPIC, userId, cartData);
    }

}
