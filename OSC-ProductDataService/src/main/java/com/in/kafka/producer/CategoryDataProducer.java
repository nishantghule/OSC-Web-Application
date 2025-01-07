package com.in.kafka.producer;

import com.in.configs.AppConstants;
import com.in.kafka.avro.category.CategoryDetails;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class CategoryDataProducer {
    private final KafkaTemplate<String, Object> kafkaTemplate;
    public void publishCategoryData(String categoryId, CategoryDetails categoryDetails){
        kafkaTemplate.send(AppConstants.CATEGORY_DETAILS_TOPIC, categoryId, categoryDetails);
    }
}
