package com.intive.rental;

import com.intive.rental.dto.FamilyRental;
import com.intive.rental.dto.SimpleRental;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

import java.util.Properties;

@Configuration
public class RentalApplicationConfiguration {

    @Value("${spring.kafka.bootstrap-servers}")
    private String KAFKA_SERVER_URL;

    @Bean
    public RestTemplate restTemplate(RestTemplateBuilder builder) {
        return builder.build();
    }

    @Bean
    public KafkaProducer<String, SimpleRental> kafkaProducer() {
        return new org.apache.kafka.clients.producer.KafkaProducer<>(kafkaProperties());
    }

    @Bean
    public KafkaProducer<String, FamilyRental> kafkaFamilyProducer() {
        return new org.apache.kafka.clients.producer.KafkaProducer<>(kafkaProperties());
    }

    private Properties kafkaProperties() {
        Properties props = new Properties();
        props.put("bootstrap.servers", KAFKA_SERVER_URL);
        props.put("key.serializer",
                "org.apache.kafka.common.serialization.StringSerializer");
        props.put("metadata.broker.list", KAFKA_SERVER_URL);
        props.put("serializer.class","kafka.serializer.StringEncoder");
        props.put("value.serializer",
                "org.apache.kafka.common.serialization.StringSerializer");
        return props;
    }

}
