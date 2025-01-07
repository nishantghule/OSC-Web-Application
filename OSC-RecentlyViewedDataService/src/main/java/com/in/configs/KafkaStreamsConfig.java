package com.in.configs;

import com.in.kafka.avro.RecentlyViewedHistory;
import com.in.kafka.serde.RecentlyViewedHistorySerde;
import io.confluent.kafka.serializers.AbstractKafkaSchemaSerDeConfig;
import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.common.utils.Bytes;
import org.apache.kafka.streams.KafkaStreams;
import org.apache.kafka.streams.StoreQueryParameters;
import org.apache.kafka.streams.StreamsBuilder;
import org.apache.kafka.streams.StreamsConfig;
import org.apache.kafka.streams.kstream.Consumed;
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
        props.put(StreamsConfig.APPLICATION_ID_CONFIG, "OSC-Recently-Viewed-Streams");
        props.put(StreamsConfig.BOOTSTRAP_SERVERS_CONFIG, AppConstants.BOOTSTRAP_SERVERS);
        props.put(AbstractKafkaSchemaSerDeConfig.SCHEMA_REGISTRY_URL_CONFIG, AppConstants.SCHEMA_REGISTRY_URL);
        props.put(StreamsConfig.DEFAULT_KEY_SERDE_CLASS_CONFIG, Serdes.String().getClass().getName());
        props.put(StreamsConfig.DEFAULT_VALUE_SERDE_CLASS_CONFIG, RecentlyViewedHistorySerde.recentlyViewedHistoryAvroSerde().getClass().getName());
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
        // Defining the KTables
        recentlyViewedKTable(streamsBuilder);

        KafkaStreams kafkaStreams = new KafkaStreams(streamsBuilder.build(), streamsProperties);
        kafkaStreams.start();

        Runtime.getRuntime().addShutdownHook(new Thread(kafkaStreams::close));
        return kafkaStreams;
    }


    @Bean(name = "recentlyViewedKTable")
    public KTable<String, RecentlyViewedHistory> recentlyViewedKTable(@Qualifier("streamsBuilder") StreamsBuilder streamsBuilder) {
        return streamsBuilder.table(AppConstants.RECENTLY_VIEWED_HISTORY_TOPIC, Consumed.with(Serdes.String(), RecentlyViewedHistorySerde.recentlyViewedHistoryAvroSerde()),
                Materialized.<String, RecentlyViewedHistory, KeyValueStore<Bytes, byte[]>>as(AppConstants.RECENTLY_VIEWED_HISTORY_STORE)
                        .withKeySerde(Serdes.String()).withValueSerde(RecentlyViewedHistorySerde.recentlyViewedHistoryAvroSerde()));
    }

    @Bean(name = "recentlyViewedStore")
    public ReadOnlyKeyValueStore<String, RecentlyViewedHistory> recentlyViewedHistoryStore(@Qualifier("kafkaStreams") KafkaStreams kafkaStreams)
            throws InterruptedException {
        while (kafkaStreams.state() != KafkaStreams.State.RUNNING) {
            Thread.sleep(100);
        }
        return kafkaStreams.store(StoreQueryParameters.fromNameAndType(AppConstants.RECENTLY_VIEWED_HISTORY_STORE, QueryableStoreTypes.keyValueStore()));
    }
}
