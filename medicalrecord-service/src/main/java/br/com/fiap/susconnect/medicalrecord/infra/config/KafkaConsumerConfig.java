/* Copyright (c) 2024 FIAP. All rights reserved. */
package br.com.fiap.susconnect.medicalrecord.infra.config;

import br.com.fiap.susconnect.medicalrecord.core.dto.TriageClassifiedEvent;
import java.util.HashMap;
import java.util.Map;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.springframework.boot.autoconfigure.kafka.KafkaProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.support.serializer.JsonDeserializer;

/** Kafka consumer configuration for medical record events. */
@Configuration
public class KafkaConsumerConfig {

  @Bean
  public ConsumerFactory<String, TriageClassifiedEvent> consumerFactory(
      KafkaProperties kafkaProperties) {
    Map<String, Object> props = new HashMap<>(kafkaProperties.buildConsumerProperties(null));
    props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
    props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, JsonDeserializer.class);
    props.put(JsonDeserializer.TRUSTED_PACKAGES, "br.com.fiap.*");
    props.put(JsonDeserializer.VALUE_DEFAULT_TYPE, TriageClassifiedEvent.class.getName());
    return new DefaultKafkaConsumerFactory<>(props);
  }

  @Bean
  public ConcurrentKafkaListenerContainerFactory<String, TriageClassifiedEvent>
      kafkaListenerFactory(ConsumerFactory<String, TriageClassifiedEvent> cf) {
    var factory = new ConcurrentKafkaListenerContainerFactory<String, TriageClassifiedEvent>();
    factory.setConsumerFactory(cf);
    return factory;
  }
}
