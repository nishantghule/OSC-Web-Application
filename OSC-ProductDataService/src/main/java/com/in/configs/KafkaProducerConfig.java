package com.in.configs;

import io.confluent.kafka.serializers.AbstractKafkaSchemaSerDeConfig;
import io.confluent.kafka.serializers.KafkaAvroSerializer;
import org.apache.kafka.clients.admin.NewTopic;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringSerializer;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
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
    private final Logger log = LogManager.getLogger(KafkaProducerConfig.class);
    @Bean
    public KafkaAdmin.NewTopics createTopics() {
        log.info("Creating topics: {}, {}, {} ", AppConstants.PRODUCT_DETAILS_TOPIC, AppConstants.CATEGORY_DETAILS_TOPIC,AppConstants.PRODUCT_VIEW_COUNT_TOPIC);
        return new KafkaAdmin.NewTopics(
                new NewTopic(AppConstants.PRODUCT_DETAILS_TOPIC, 3, (short) 1),
                new NewTopic(AppConstants.CATEGORY_DETAILS_TOPIC, 3, (short) 1),
                new NewTopic(AppConstants.PRODUCT_VIEW_COUNT_TOPIC, 3, (short)1)
        );
    }

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
        configProps.put(ProducerConfig.ENABLE_IDEMPOTENCE_CONFIG, "true"); // Enables date duplication
        return new DefaultKafkaProducerFactory<>(configProps);
    }

    // Bean for KafkaTemplate with a custom name
    @Bean(name = "defaultKafkaTemplate")
    public KafkaTemplate<String, Object> kafkaTemplate() {
        return new KafkaTemplate<>(producerFactory());
    }

}
