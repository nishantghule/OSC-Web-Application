/*
package com.in.configs;

import com.in.kafka.avro.SessionKey;
import com.in.kafka.serdes.SessionKeySerde;
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
import org.springframework.kafka.annotation.EnableKafka;

import java.util.Properties;

@EnableKafka
@Configuration
public class SessionKafkaStreamsConfig {

    @Bean(name = "sessionStreamsProperties")
    public Properties streamsProperties() {
        Properties props = new Properties();
        props.put(StreamsConfig.APPLICATION_ID_CONFIG, "OSC-Session-Service");
        props.put(StreamsConfig.BOOTSTRAP_SERVERS_CONFIG, AppConstants.BOOTSTRAP_SERVERS);
        props.put(AbstractKafkaSchemaSerDeConfig.SCHEMA_REGISTRY_URL_CONFIG, AppConstants.SCHEMA_REGISTRY_URL);
        props.put(StreamsConfig.DEFAULT_KEY_SERDE_CLASS_CONFIG, SessionKeySerde.sessionKeyAvroSerde().getClass().getName());
        props.put(StreamsConfig.DEFAULT_VALUE_SERDE_CLASS_CONFIG, Serdes.String().getClass().getName());
        props.put(StreamsConfig.COMMIT_INTERVAL_MS_CONFIG, 15);
        props.put(StreamsConfig.CACHE_MAX_BYTES_BUFFERING_DOC, 100L);
        return props;
    }

    @Bean(name = "sessionStreamsBuilder")
    public StreamsBuilder streamsBuilder() {
        return new StreamsBuilder();
    }

    @Bean(name = "sessionKafkaStreams")
    public KafkaStreams kafkaStreams(@Qualifier("sessionStreamsProperties")Properties streamsProperties,
                                     @Qualifier("sessionStreamsBuilder") StreamsBuilder streamsBuilder) {
        sessionKTable(streamsBuilder);

        KafkaStreams kafkaStreams = new KafkaStreams(streamsBuilder.build(), streamsProperties);
        kafkaStreams.start();

        Runtime.getRuntime().addShutdownHook(new Thread(kafkaStreams::close));
        return kafkaStreams;
    }

    @Bean(name = "sessionKTable")
    public KTable<SessionKey, String> sessionKTable(@Qualifier("sessionStreamsBuilder")StreamsBuilder streamsBuilder) {
        return streamsBuilder.table(AppConstants.SESSION_DETAILS_TOPIC, Consumed.with(SessionKeySerde.sessionKeyAvroSerde(), Serdes.String()),
                Materialized.<SessionKey, String, KeyValueStore<Bytes, byte[]>>as(AppConstants.SESSION_DETAILS_STORE)
                        .withKeySerde(SessionKeySerde.sessionKeyAvroSerde()).withValueSerde(Serdes.String()));
    }

    @Bean(name = "sessionStore")
    public ReadOnlyKeyValueStore<SessionKey, String> sessionStore(@Qualifier("sessionKafkaStreams") KafkaStreams kafkaStreams)
            throws InterruptedException {

        while (kafkaStreams.state() != KafkaStreams.State.RUNNING) {
            Thread.sleep(100);
        }
        return kafkaStreams.store(StoreQueryParameters.fromNameAndType(AppConstants.SESSION_DETAILS_STORE, QueryableStoreTypes.keyValueStore()));
    }
}
*/
