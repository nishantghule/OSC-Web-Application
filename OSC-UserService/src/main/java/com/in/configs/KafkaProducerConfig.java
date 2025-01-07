package com.in.configs;

import io.confluent.kafka.serializers.AbstractKafkaSchemaSerDeConfig;
import io.confluent.kafka.serializers.KafkaAvroSerializer;
import org.apache.kafka.clients.admin.NewTopic;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaAdmin;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;

import java.util.HashMap;
import java.util.Map;

@EnableKafka
@Configuration
public class KafkaProducerConfig {

    // Bean for creating topics with a custom name
    @Bean(name = "userOtpAndSessionTopics")
    public KafkaAdmin.NewTopics createTopics() {
        System.out.println("Creating topics: " + AppConstants.USER_DETAILS_TOPIC + ", " + AppConstants.OTP_TOPIC);
        return new KafkaAdmin.NewTopics(
                new NewTopic(AppConstants.USER_DETAILS_TOPIC, 3, (short) 1),
                new NewTopic(AppConstants.OTP_TOPIC, 3, (short) 1),
                new NewTopic(AppConstants.SESSION_DETAILS_TOPIC, 3, (short) 1)
        );
    }

    // Bean for KafkaAdmin with a custom name
    @Bean(name = "kafkaAdminBean")
    public KafkaAdmin kafkaAdmin() {
        Map<String, Object> configs = new HashMap<>();
        configs.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, AppConstants.BOOTSTRAP_SERVERS);
        return new KafkaAdmin(configs);
    }

    // Bean for ProducerFactory with a custom name
    @Bean(name = "defaultProducerFactory")
    public ProducerFactory<String, Object> producerFactory() {
        Map<String, Object> configProps = new HashMap<>();
        configProps.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, AppConstants.BOOTSTRAP_SERVERS);
        configProps.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        configProps.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, KafkaAvroSerializer.class);
        configProps.put(AbstractKafkaSchemaSerDeConfig.SCHEMA_REGISTRY_URL_CONFIG, AppConstants.SCHEMA_REGISTRY_URL);
        return new DefaultKafkaProducerFactory<>(configProps);
    }

    // Bean for KafkaTemplate with a custom name
    @Bean(name = "defaultKafkaTemplate")
    public KafkaTemplate<String, Object> kafkaTemplate() {
        return new KafkaTemplate<>(producerFactory());
    }

    // Bean for ProducerFactory used for session data with a custom name
    @Bean(name = "sessionProducerFactory")
    public ProducerFactory<Object, String> sessionProducerFactory() {
        Map<String, Object> configProps = new HashMap<>();
        configProps.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, AppConstants.BOOTSTRAP_SERVERS);
        configProps.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, KafkaAvroSerializer.class);
        configProps.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        configProps.put(AbstractKafkaSchemaSerDeConfig.SCHEMA_REGISTRY_URL_CONFIG, AppConstants.SCHEMA_REGISTRY_URL);
        return new DefaultKafkaProducerFactory<>(configProps);
    }

    // Bean for KafkaTemplate used for session data with a custom name
    @Bean(name = "sessionKafkaTemplate")
    public KafkaTemplate<Object, String> sessionKafkaTemplate() {
        return new KafkaTemplate<>(sessionProducerFactory());
    }

}
