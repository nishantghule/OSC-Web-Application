package com.in.configs;

import com.in.kafka.avro.cart.CartData;
import com.in.kafka.serdes.CartDataSerde;
import io.confluent.kafka.serializers.AbstractKafkaSchemaSerDeConfig;
import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.common.utils.Bytes;
import org.apache.kafka.streams.KafkaStreams;
import org.apache.kafka.streams.StoreQueryParameters;
import org.apache.kafka.streams.StreamsBuilder;
import org.apache.kafka.streams.StreamsConfig;
import org.apache.kafka.streams.kstream.Consumed;
import org.apache.kafka.streams.kstream.GlobalKTable;
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
        props.put(StreamsConfig.APPLICATION_ID_CONFIG, "OSC-CartDataService-Stream");
        props.put(StreamsConfig.BOOTSTRAP_SERVERS_CONFIG, AppConstants.BOOTSTRAP_SERVERS);
        props.put(AbstractKafkaSchemaSerDeConfig.SCHEMA_REGISTRY_URL_CONFIG, AppConstants.SCHEMA_REGISTRY_URL);
        props.put(StreamsConfig.DEFAULT_KEY_SERDE_CLASS_CONFIG, Serdes.String().getClass().getName());
        props.put(StreamsConfig.DEFAULT_VALUE_SERDE_CLASS_CONFIG, CartDataSerde.cartDataAvroSerde().getClass().getName());
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
        // Defining the KTables for cart data
        cartDataKTable(streamsBuilder);

        KafkaStreams kafkaStreams = new KafkaStreams(streamsBuilder.build(), streamsProperties);
        kafkaStreams.start();

        Runtime.getRuntime().addShutdownHook(new Thread(kafkaStreams::close));
        return kafkaStreams;
    }

    // cart data KTable
    @Bean(name = "cartDataKTable")
    public GlobalKTable<String, CartData> cartDataKTable(@Qualifier("streamsBuilder") StreamsBuilder streamsBuilder) {
        return streamsBuilder.globalTable(AppConstants.CART_DETAILS_TOPIC, Consumed.with(Serdes.String(), CartDataSerde.cartDataAvroSerde()),
                Materialized.<String, CartData, KeyValueStore<Bytes, byte[]>>as(AppConstants.CART_DETAILS_STORE)
                        .withKeySerde(Serdes.String()).withValueSerde(CartDataSerde.cartDataAvroSerde()));
    }

    // cart data Store
    @Bean(name = "cartDataStore")
    public ReadOnlyKeyValueStore<String, CartData> cartDataStore(@Qualifier("kafkaStreams") KafkaStreams kafkaStreams)
            throws InterruptedException {
        while (kafkaStreams.state() != KafkaStreams.State.RUNNING) {
            Thread.sleep(100);
        }
        return kafkaStreams.store(StoreQueryParameters.fromNameAndType(AppConstants.CART_DETAILS_STORE, QueryableStoreTypes.keyValueStore()));
    }


}
