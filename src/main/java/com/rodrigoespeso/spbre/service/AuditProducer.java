package com.rodrigoespeso.spbre.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
public class AuditProducer {

    @Autowired
    private KafkaTemplate<String, Object> kafkaTemplate;

    // Enviar mensaje al tópico 'transfer-audit'
    public void sendMessage(String topic, Object message) {
        kafkaTemplate.send(topic, message);
    }
}