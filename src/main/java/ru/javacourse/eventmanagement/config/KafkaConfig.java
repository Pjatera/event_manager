package ru.javacourse.eventmanagement.config;

import lombok.RequiredArgsConstructor;
import org.apache.kafka.clients.admin.NewTopic;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.kafka.config.TopicBuilder;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;
import org.springframework.kafka.support.serializer.JsonSerializer;
import ru.javacourse.eventnotoficationcore.kafka.EventChangeKafkaMessage;


import java.util.HashMap;
import java.util.Map;

@Configuration
@RequiredArgsConstructor
public class KafkaConfig {
    private final Environment environment;


    @Bean
    NewTopic createdTopic() {
        return TopicBuilder
                .name("ru.queuing.event_notification.event_change.read")
                .partitions(3)
                .replicas(1)
                //.configs(Map.of("min.insync.replicas", "2"))
                .build();
    }


    Map<String, Object> producerConfigs() {
        Map<String, Object> config = new HashMap<>();
        config.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG,
                environment.getProperty("spring.kafka.bootstrap-servers"));
        config.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        config.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, JsonSerializer.class);
        config.put(ProducerConfig.ACKS_CONFIG,
                environment.getProperty("spring.kafka.producer.acks"));
        config.put(ProducerConfig.DELIVERY_TIMEOUT_MS_CONFIG,
                environment.getProperty("spring.kafka.producer.properties.delivery.timeout.ms"));
        config.put(ProducerConfig.LINGER_MS_CONFIG,
                environment.getProperty("spring.kafka.producer.properties.linger.ms"));
        config.put(ProducerConfig.REQUEST_TIMEOUT_MS_CONFIG,
                environment.getProperty("spring.kafka.producer.properties.request.timeout.ms"));
        config.put(ProducerConfig.ENABLE_IDEMPOTENCE_CONFIG, true);
        config.put(ProducerConfig.MAX_IN_FLIGHT_REQUESTS_PER_CONNECTION,
                environment.getProperty("spring.kafka.producer.properties.max.in.flight.request.per.connection"));
        return config;
    }

    @Bean
    ProducerFactory<String, EventChangeKafkaMessage> producerFactory() {
        return new DefaultKafkaProducerFactory<>(producerConfigs());
    }

    @Bean
    KafkaTemplate<String, EventChangeKafkaMessage> kafkaTemplate() {
        return new KafkaTemplate<>(producerFactory());
    }

}
