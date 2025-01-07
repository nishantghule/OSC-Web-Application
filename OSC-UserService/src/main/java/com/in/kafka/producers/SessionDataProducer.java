package com.in.kafka.producers;

import com.in.configs.AppConstants;
import com.in.kafka.avro.SessionKey;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
public class SessionDataProducer {
    @Qualifier("sessionKafkaTemplate")
    private final KafkaTemplate<Object, String> kafkaTemplate;

    public SessionDataProducer(@Qualifier("sessionKafkaTemplate")KafkaTemplate<Object, String> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    public void publishDataToTopic(SessionKey sessionKey, String sessionId){
        kafkaTemplate.send(AppConstants.SESSION_DETAILS_TOPIC, sessionKey, sessionId);
    }
    public void removeDataFromTopic(SessionKey sessionKey){
        kafkaTemplate.send(AppConstants.SESSION_DETAILS_TOPIC, sessionKey, null);
    }
}
