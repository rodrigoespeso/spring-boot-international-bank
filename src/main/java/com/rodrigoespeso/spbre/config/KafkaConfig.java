package com.rodrigoespeso.spbre.config;

import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;

import java.util.HashMap;
import java.util.Map;

@Configuration
public class KafkaConfig {

    // Configuración de Kafka para el productor
    @Bean
    public ProducerFactory<String, String> producerFactory() {
        // Mapa para configurar las propiedades del productor
        Map<String, Object> configProps = new HashMap<>();
        
        // Dirección del servidor de Kafka (puerto 9092 por defecto)
        configProps.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092");
        
        // Serializador para las claves (en este caso, String)
        configProps.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        
        // Serializador para los valores (también String)
        configProps.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        
        // Devuelve una fábrica de productores configurada
        return new DefaultKafkaProducerFactory<>(configProps);
    }

    // Proporciona una plantilla de Kafka para enviar mensajes
    @Bean
    public KafkaTemplate<String, String> kafkaTemplate() {
        // Crea un KafkaTemplate utilizando la fábrica de productores configurada
        return new KafkaTemplate<>(producerFactory());
    }
}