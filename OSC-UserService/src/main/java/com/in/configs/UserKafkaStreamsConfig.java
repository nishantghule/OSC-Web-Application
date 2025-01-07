/*
package com.in.configs;

import java.util.Properties;

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
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Bean;
import com.in.kafka.avro.Otp;
import com.in.kafka.avro.User;
import com.in.kafka.serdes.UserSerde;
import com.in.kafka.serdes.OtpSerde;
import io.confluent.kafka.serializers.AbstractKafkaSchemaSerDeConfig;

@Configuration
public class UserKafkaStreamsConfig {

    @Bean(name = "defaultStreamsProperties")
    public Properties streamsProperties() {
        Properties props = new Properties();
        props.put(StreamsConfig.APPLICATION_ID_CONFIG, "OSC-Project");
        props.put(StreamsConfig.BOOTSTRAP_SERVERS_CONFIG, AppConstants.BOOTSTRAP_SERVERS);
        props.put(AbstractKafkaSchemaSerDeConfig.SCHEMA_REGISTRY_URL_CONFIG, AppConstants.SCHEMA_REGISTRY_URL);
        props.put(StreamsConfig.DEFAULT_KEY_SERDE_CLASS_CONFIG, Serdes.String().getClass().getName());
        props.put(StreamsConfig.DEFAULT_VALUE_SERDE_CLASS_CONFIG, UserSerde.userAvroSerde().getClass().getName());
        props.put(StreamsConfig.COMMIT_INTERVAL_MS_CONFIG, 15);
        props.put(StreamsConfig.CACHE_MAX_BYTES_BUFFERING_DOC, 100L);
        return props;
    }

    @Bean(name = "defaultStreamsBuilder")
    public StreamsBuilder streamsBuilder() {
        return new StreamsBuilder();
    }

    @Bean(name = "defaultKafkaStreams")
    public KafkaStreams kafkaStreams(@Qualifier("defaultStreamsProperties") Properties streamsProperties,
                                     @Qualifier("defaultStreamsBuilder")StreamsBuilder streamsBuilder) {
        otpKTable(streamsBuilder);
        userKTable(streamsBuilder);

        KafkaStreams kafkaStreams = new KafkaStreams(streamsBuilder.build(), streamsProperties);
        kafkaStreams.start();

        Runtime.getRuntime().addShutdownHook(new Thread(kafkaStreams::close));
        return kafkaStreams;
    }

    @Bean(name = "userOtpKTable")
    public KTable<String, Otp> otpKTable(@Qualifier("defaultStreamsBuilder")StreamsBuilder streamsBuilder) {
        return streamsBuilder.table(AppConstants.OTP_TOPIC, Consumed.with(Serdes.String(), OtpSerde.otpAvroSerde()),
                Materialized.<String, Otp, KeyValueStore<Bytes, byte[]>>as(AppConstants.OTP_STORE)
                        .withKeySerde(Serdes.String()).withValueSerde(OtpSerde.otpAvroSerde()));
    }

    @Bean(name = "userKTable")
    public KTable<String, User> userKTable(@Qualifier("defaultStreamsBuilder")StreamsBuilder streamsBuilder) {
        return streamsBuilder.table(AppConstants.USER_DETAILS_TOPIC, Consumed.with(Serdes.String(), UserSerde.userAvroSerde()),
                Materialized.<String, User, KeyValueStore<Bytes, byte[]>>as(AppConstants.USER_DETAILS_STORE)
                        .withKeySerde(Serdes.String()).withValueSerde(UserSerde.userAvroSerde()));
    }

    @Bean(name = "userOtpStore")
    public ReadOnlyKeyValueStore<String, Otp> otpStore(@Qualifier("defaultKafkaStreams") KafkaStreams kafkaStreams)
            throws InterruptedException {
        while (kafkaStreams.state() != KafkaStreams.State.RUNNING) {
            Thread.sleep(100);
        }
        return kafkaStreams.store(StoreQueryParameters.fromNameAndType(AppConstants.OTP_STORE, QueryableStoreTypes.keyValueStore()));
    }

    @Bean(name = "userDetailsStore")
    public ReadOnlyKeyValueStore<String, User> userDetailsStore(@Qualifier("defaultKafkaStreams")KafkaStreams kafkaStreams)
            throws InterruptedException {
        while (kafkaStreams.state() != KafkaStreams.State.RUNNING) {
            Thread.sleep(100);
        }
        return kafkaStreams.store(StoreQueryParameters.fromNameAndType(AppConstants.USER_DETAILS_STORE, QueryableStoreTypes.keyValueStore()));
    }
}
*/
