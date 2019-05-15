package com.matias.rental;

import com.matias.rental.dto.Rental;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

import java.util.Properties;

@Configuration
public class RentalApplicationConfiguration {

    @Bean
    public RestTemplate restTemplate(RestTemplateBuilder builder) {
        // Do any additional configuration here
        return builder.build();
    }

    @Bean
    public KafkaProducer<String, Rental> kafkaProducer() {
        String KAFKA_SERVER = "server-url";
        final Properties props = new Properties();
        props.put("bootstrap.servers", KAFKA_SERVER);
        props.put("key.serializer",
                "org.apache.kafka.common.serialization.StringSerializer");
        props.put("metadata.broker.list", KAFKA_SERVER);
        props.put("serializer.class", "kafka.serializer.StringEncoder");
        props.put("value.serializer",
                "org.apache.kafka.common.serialization.StringSerializer");
        return new org.apache.kafka.clients.producer.KafkaProducer<>(props);
    }

}
