package com.in.kafka.serdes;

import com.in.kafka.avro.category.CategoryDetails;
import io.confluent.kafka.serializers.AbstractKafkaSchemaSerDeConfig;
import io.confluent.kafka.streams.serdes.avro.SpecificAvroSerde;
import org.apache.kafka.common.serialization.Serde;
import org.apache.kafka.common.serialization.Serdes;

import java.util.Collections;
import java.util.Map;

public class CategorySerde extends Serdes {
    public static Serde<CategoryDetails> categoryAvroSerde() {
        Serde<CategoryDetails> serde = new SpecificAvroSerde<>();
        Map<String, String> serdeConfig = Collections
                .singletonMap(AbstractKafkaSchemaSerDeConfig.SCHEMA_REGISTRY_URL_CONFIG, "http://192.168.99.223:18081");
        serde.configure(serdeConfig, false);
        return serde;
    }
}
