package com.in.kafka.producers;

import com.in.configs.AppConstants;
import com.in.kafka.avro.User;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
public class UserDataProducer {
    @Qualifier("defaultKafkaTemplate")
    private final KafkaTemplate<String, Object> kafkaTemplate;

    public UserDataProducer(@Qualifier("defaultKafkaTemplate")KafkaTemplate<String, Object> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    public void publishToTopic(String userId, User user){
        kafkaTemplate.send(AppConstants.USER_DETAILS_TOPIC, userId, user);
    }
    public void removeUserDataFromTopic(String userId){
        kafkaTemplate.send(AppConstants.USER_DETAILS_TOPIC, userId, null);
    }
}
