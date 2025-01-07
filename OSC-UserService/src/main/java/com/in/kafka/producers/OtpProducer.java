package com.in.kafka.producers;

import com.in.configs.AppConstants;
import com.in.dtos.OtpDataDTO;
import com.in.kafka.avro.Otp;
import com.in.mappers.UserMapper;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
public class OtpProducer {
    private final Logger log = LogManager.getLogger(OtpProducer.class);
    @Qualifier("defaultKafkaTemplate")
    private final KafkaTemplate<String, Object> kafkaTemplate;

    public OtpProducer(@Qualifier("defaultKafkaTemplate")KafkaTemplate<String, Object> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    public void publishToTopic(String userId, Otp otp){
        kafkaTemplate.send(AppConstants.OTP_TOPIC, userId, otp);
    }

    public void removeOtpDataFromTopic(String userId) {
        kafkaTemplate.send(AppConstants.OTP_TOPIC, userId, null);
    }
    // to set Password at the time Registration
    public boolean publishToOtpTopicForRegistration(OtpDataDTO request) {
        try {
            kafkaTemplate.send(AppConstants.OTP_TOPIC,request.getUserId(), UserMapper.requestToOtpForRegistration(request));
            log.info("otp sent to topic for registration: {} " , request);
            return true;
        } catch (Exception e) {
            log.error("Error in publishing data in otp-topic at the time of registration");
            return false;
        }
    }
    // to set Password at the time forget password
    public boolean publishToOtpTopicForForgetPassword(OtpDataDTO request) {
        try {
            kafkaTemplate.send(AppConstants.OTP_TOPIC,request.getEmail(), UserMapper.requestToOtpForForgetPassword(request));
            log.info("otp sent to topic for forget password: {} " , request);
            return true;
        } catch (Exception e) {
            log.error("Error in publishing data in otp-topic at the time of forget password");
            return false;
        }
    }
}
