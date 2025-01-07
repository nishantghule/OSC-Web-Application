package com.in.kafka.consumer;

import com.in.kafka.avro.Otp;
import lombok.RequiredArgsConstructor;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.mail.MailException;
import org.springframework.stereotype.Component;

import com.in.configs.AppConstants;
import com.in.services.EmailSender;
@RequiredArgsConstructor
@Component
public class MailServiceConsumer {
    private final Logger log = LogManager.getLogger(MailServiceConsumer.class);
    private final EmailSender emailSender;

    @KafkaListener(topics = AppConstants.OTP_TOPIC, groupId = AppConstants.GROUP_ID)
    public void read(ConsumerRecord<String, Otp> consumerRecord) {
        try {
            log.info("Received OTP details for user : {}, Failed Attempts : {}, OTP Type : {}, OTP : {}", consumerRecord.value().getEmail(),
                    consumerRecord.value().getFailedAttempts(), consumerRecord.value().getOtpType(), consumerRecord.value().getOtp());

            if (consumerRecord.value().getFailedAttempts() == 0
                    && consumerRecord.value().getOtpType().toString().equals("REGISTRATION")) {

                emailSender.sendRegistrationEmail(consumerRecord.value().getEmail().toString(),
                        consumerRecord.key(),
                        consumerRecord.value().getOtp().toString());
                log.info("Registration email sent to user {} with OTP: {}", consumerRecord.value().getEmail(), consumerRecord.value().getOtp());

            } else if (consumerRecord.value().getFailedAttempts() == 0
                    && consumerRecord.value().getOtpType().toString().equals("RESET_PASSWORD")) {

                emailSender.sendResetPasswordEmail(consumerRecord.key(),
                        consumerRecord.value().getOtp().toString());
                log.info("Reset password email sent to user {} with OTP: {}", consumerRecord.value().getEmail(),consumerRecord.value().getOtp());

            } else if (consumerRecord.value().getFailedAttempts() >= 3) {
                log.info("You have tried the maximum number of attempts for OTP validation");
            } else {
                log.info("Conditions not met for sending the email.");
            }
        } catch (MailException e) {
            log.error("Error in sending email! MailException caught.");
        }  catch(NullPointerException e){
            log.error("OTP message has been deleted. Generate new OTP!");
        } catch (Exception e) {
            log.error("Unexpected error while processing OTP message!");
        }
    }
}
