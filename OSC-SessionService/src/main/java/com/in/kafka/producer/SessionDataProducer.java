package com.in.kafka.producer;

import com.in.configs.AppConstants;
import com.in.kafka.avro.SessionKey;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class SessionDataProducer {
    private final KafkaTemplate<Object, String> kafkaTemplate;
    public void publishDataToTopic(SessionKey sessionKey, String sessionId){
        kafkaTemplate.send(AppConstants.SESSION_DETAILS_TOPIC, sessionKey, sessionId);
    }
    public void removeDataFromTopic(SessionKey sessionKey){
        kafkaTemplate.send(AppConstants.SESSION_DETAILS_TOPIC, sessionKey, null);
    }
}
