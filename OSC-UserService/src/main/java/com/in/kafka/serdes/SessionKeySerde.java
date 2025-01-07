package com.in.kafka.serdes;

import com.in.kafka.avro.SessionKey;
import io.confluent.kafka.serializers.AbstractKafkaSchemaSerDeConfig;
import io.confluent.kafka.streams.serdes.avro.SpecificAvroSerde;
import org.apache.kafka.common.serialization.Serde;

import java.util.Collections;
import java.util.Map;


public class SessionKeySerde {
    public static Serde<SessionKey> sessionKeyAvroSerde() {
        Serde<SessionKey> serde = new SpecificAvroSerde<>();
        Map<String, String> serdeConfig = Collections
                .singletonMap(AbstractKafkaSchemaSerDeConfig.SCHEMA_REGISTRY_URL_CONFIG, "http://192.168.99.223:18081");
        serde.configure(serdeConfig, false);
        return serde;
    }
}
