package com.in.configs;

import com.in.kafka.avro.Otp;
import com.in.kafka.avro.User;
import com.in.kafka.avro.SessionKey;
import com.in.kafka.serdes.OtpSerde;
import com.in.kafka.serdes.UserSerde;
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

import java.util.Properties;

@Configuration
public class KafkaStreamsConfig {

    @Bean(name = "streamsProperties")
    public Properties streamsProperties() {
        Properties props = new Properties();
        props.put(StreamsConfig.APPLICATION_ID_CONFIG, "OSC-Combined-Streams");
        props.put(StreamsConfig.BOOTSTRAP_SERVERS_CONFIG, AppConstants.BOOTSTRAP_SERVERS);
        props.put(AbstractKafkaSchemaSerDeConfig.SCHEMA_REGISTRY_URL_CONFIG, AppConstants.SCHEMA_REGISTRY_URL);
        props.put(StreamsConfig.DEFAULT_KEY_SERDE_CLASS_CONFIG, Serdes.String().getClass().getName());
        props.put(StreamsConfig.DEFAULT_VALUE_SERDE_CLASS_CONFIG, UserSerde.userAvroSerde().getClass().getName());
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
        userKTable(streamsBuilder);
        otpKTable(streamsBuilder);
        sessionKTable(streamsBuilder);

        KafkaStreams kafkaStreams = new KafkaStreams(streamsBuilder.build(), streamsProperties);
        kafkaStreams.start();

        Runtime.getRuntime().addShutdownHook(new Thread(kafkaStreams::close));
        return kafkaStreams;
    }

    // User KTable
    @Bean(name = "userKTable")
    public KTable<String, User> userKTable(@Qualifier("streamsBuilder") StreamsBuilder streamsBuilder) {
        return streamsBuilder.table(AppConstants.USER_DETAILS_TOPIC, Consumed.with(Serdes.String(), UserSerde.userAvroSerde()),
                Materialized.<String, User, KeyValueStore<Bytes, byte[]>>as(AppConstants.USER_DETAILS_STORE)
                        .withKeySerde(Serdes.String()).withValueSerde(UserSerde.userAvroSerde()));
    }

    // OTP KTable
    @Bean(name = "userOtpKTable")
    public KTable<String, Otp> otpKTable(@Qualifier("streamsBuilder") StreamsBuilder streamsBuilder) {
        return streamsBuilder.table(AppConstants.OTP_TOPIC, Consumed.with(Serdes.String(), OtpSerde.otpAvroSerde()),
                Materialized.<String, Otp, KeyValueStore<Bytes, byte[]>>as(AppConstants.OTP_STORE)
                        .withKeySerde(Serdes.String()).withValueSerde(OtpSerde.otpAvroSerde()));
    }

    // Session KTable
    @Bean(name = "sessionKTable")
    public KTable<SessionKey, String> sessionKTable(@Qualifier("streamsBuilder") StreamsBuilder streamsBuilder) {
        return streamsBuilder.table(AppConstants.SESSION_DETAILS_TOPIC, Consumed.with(SessionKeySerde.sessionKeyAvroSerde(), Serdes.String()),
                Materialized.<SessionKey, String, KeyValueStore<Bytes, byte[]>>as(AppConstants.SESSION_DETAILS_STORE)
                        .withKeySerde(SessionKeySerde.sessionKeyAvroSerde()).withValueSerde(Serdes.String()));
    }

    // User Details Store
    @Bean(name = "userDetailsStore")
    public ReadOnlyKeyValueStore<String, User> userDetailsStore(@Qualifier("kafkaStreams") KafkaStreams kafkaStreams)
            throws InterruptedException {
        while (kafkaStreams.state() != KafkaStreams.State.RUNNING) {
            Thread.sleep(100);
        }
        return kafkaStreams.store(StoreQueryParameters.fromNameAndType(AppConstants.USER_DETAILS_STORE, QueryableStoreTypes.keyValueStore()));
    }

    // OTP Store
    @Bean(name = "userOtpStore")
    public ReadOnlyKeyValueStore<String, Otp> otpStore(@Qualifier("kafkaStreams") KafkaStreams kafkaStreams)
            throws InterruptedException {
        while (kafkaStreams.state() != KafkaStreams.State.RUNNING) {
            Thread.sleep(100);
        }
        return kafkaStreams.store(StoreQueryParameters.fromNameAndType(AppConstants.OTP_STORE, QueryableStoreTypes.keyValueStore()));
    }

    // Session Store
    @Bean(name = "sessionStore")
    public ReadOnlyKeyValueStore<SessionKey, String> sessionStore(@Qualifier("kafkaStreams") KafkaStreams kafkaStreams)
            throws InterruptedException {
        while (kafkaStreams.state() != KafkaStreams.State.RUNNING) {
            Thread.sleep(100);
        }
        return kafkaStreams.store(StoreQueryParameters.fromNameAndType(AppConstants.SESSION_DETAILS_STORE, QueryableStoreTypes.keyValueStore()));
    }
}
