package com.in.configs;

import com.in.kafka.avro.category.CategoryDetails;
import com.in.kafka.avro.product.ProductDetails;
import com.in.kafka.avro.product.ProductViewCount;
import com.in.kafka.serdes.CategorySerde;
import com.in.kafka.serdes.ProductViewCountSerde;
import com.in.kafka.serdes.ProductsSerde;
import io.confluent.kafka.serializers.AbstractKafkaSchemaSerDeConfig;
import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.common.utils.Bytes;
import org.apache.kafka.streams.KafkaStreams;
import org.apache.kafka.streams.StoreQueryParameters;
import org.apache.kafka.streams.StreamsBuilder;
import org.apache.kafka.streams.StreamsConfig;
import org.apache.kafka.streams.kstream.Consumed;
import org.apache.kafka.streams.kstream.GlobalKTable;
import org.apache.kafka.streams.kstream.KTable;
import org.apache.kafka.streams.kstream.Materialized;
import org.apache.kafka.streams.state.KeyValueStore;
import org.apache.kafka.streams.state.QueryableStoreTypes;
import org.apache.kafka.streams.state.ReadOnlyKeyValueStore;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Properties;

@Configuration
public class KafkaStreamsConfig {
    @Bean(name = "streamsProperties")
    public Properties streamsProperties() {
        Properties props = new Properties();
        props.put(StreamsConfig.APPLICATION_ID_CONFIG, "OSC-Products-Streams");
        props.put(StreamsConfig.BOOTSTRAP_SERVERS_CONFIG, AppConstants.BOOTSTRAP_SERVERS);
        props.put(AbstractKafkaSchemaSerDeConfig.SCHEMA_REGISTRY_URL_CONFIG, AppConstants.SCHEMA_REGISTRY_URL);
        props.put(StreamsConfig.DEFAULT_KEY_SERDE_CLASS_CONFIG, Serdes.String().getClass().getName());
        props.put(StreamsConfig.DEFAULT_VALUE_SERDE_CLASS_CONFIG, ProductsSerde.productAvroSerde().getClass().getName());
        props.put(StreamsConfig.COMMIT_INTERVAL_MS_CONFIG, 15);
        props.put(StreamsConfig.CACHE_MAX_BYTES_BUFFERING_DOC, 100L);
        return props;
    }

    @Bean(name = "streamsBuilder")
    public StreamsBuilder streamsBuilder() {
        return new StreamsBuilder();
    }

    @Bean(name = "kafkaStreams")
    public KafkaStreams kafkaStreams(@Qualifier("streamsProperties") Properties streamsProperties,
                                     @Qualifier("streamsBuilder") StreamsBuilder streamsBuilder) {
        // Defining the KTables for user, otp, and session
        productGlobalKTable(streamsBuilder);
        categoryGlobalKTable(streamsBuilder);
        productViewCountKTable(streamsBuilder);

        KafkaStreams kafkaStreams = new KafkaStreams(streamsBuilder.build(), streamsProperties);
        kafkaStreams.start();

        Runtime.getRuntime().addShutdownHook(new Thread(kafkaStreams::close));
        return kafkaStreams;
    }

    // Products Global KTable
    @Bean(name = "productsGlobalKTable")
    public GlobalKTable<String, ProductDetails> productGlobalKTable(@Qualifier("streamsBuilder") StreamsBuilder streamsBuilder) {
        return streamsBuilder.globalTable(AppConstants.PRODUCT_DETAILS_TOPIC, Consumed.with(Serdes.String(), ProductsSerde.productAvroSerde()),
                Materialized.<String, ProductDetails, KeyValueStore<Bytes, byte[]>>as(AppConstants.PRODUCT_DETAILS_STORE)
                        .withKeySerde(Serdes.String()).withValueSerde(ProductsSerde.productAvroSerde()));
    }

    // Products Details Store
    @Bean(name = "productsDetailsStore")
    public ReadOnlyKeyValueStore<String, ProductDetails> productDetailsStore(@Qualifier("kafkaStreams") KafkaStreams kafkaStreams)
            throws InterruptedException {
        while (kafkaStreams.state() != KafkaStreams.State.RUNNING) {
            Thread.sleep(100);
        }
        return kafkaStreams.store(StoreQueryParameters.fromNameAndType(AppConstants.PRODUCT_DETAILS_STORE, QueryableStoreTypes.keyValueStore()));
    }

    // Category Global KTable
    @Bean(name = "categoryGlobalKTable")
    public GlobalKTable<String, CategoryDetails> categoryGlobalKTable(@Qualifier("streamsBuilder") StreamsBuilder streamsBuilder) {
        return streamsBuilder.globalTable(AppConstants.CATEGORY_DETAILS_TOPIC, Consumed.with(Serdes.String(), CategorySerde.categoryAvroSerde()),
                Materialized.<String, CategoryDetails, KeyValueStore<Bytes, byte[]>>as(AppConstants.CATEGORY_DETAILS_STORE)
                        .withKeySerde(Serdes.String()).withValueSerde(CategorySerde.categoryAvroSerde()));
    }

    // Category Details Store
    @Bean(name = "categoryDetailsStore")
    public ReadOnlyKeyValueStore<String, CategoryDetails> categoryDetailsStore(@Qualifier("kafkaStreams") KafkaStreams kafkaStreams)
            throws InterruptedException {
        while (kafkaStreams.state() != KafkaStreams.State.RUNNING) {
            Thread.sleep(100);
        }
        return kafkaStreams.store(StoreQueryParameters.fromNameAndType(AppConstants.CATEGORY_DETAILS_STORE, QueryableStoreTypes.keyValueStore()));
    }

    @Bean("productViewCountKTable")
    public KTable<String, ProductViewCount> productViewCountKTable(@Qualifier("streamsBuilder") StreamsBuilder streamsBuilder) {
        return streamsBuilder.table(AppConstants.PRODUCT_VIEW_COUNT_TOPIC, Consumed.with(Serdes.String(), ProductViewCountSerde.productViewCountAvroSerde()),
                Materialized.<String, ProductViewCount, KeyValueStore<Bytes, byte[]>>as(AppConstants.PRODUCT_VIEW_COUNT_STORE)
                        .withKeySerde(Serdes.String()).withValueSerde(ProductViewCountSerde.productViewCountAvroSerde()));
    }

    @Bean("productViewCountStore")
    public ReadOnlyKeyValueStore<String, ProductViewCount> productViewCountStore(@Qualifier("kafkaStreams") KafkaStreams kafkaStreams)
            throws InterruptedException {
        while(kafkaStreams.state() != KafkaStreams.State.RUNNING){
            Thread.sleep(100);
        }
        return kafkaStreams.store(StoreQueryParameters.fromNameAndType(AppConstants.PRODUCT_VIEW_COUNT_STORE, QueryableStoreTypes.keyValueStore()));
    }
}
