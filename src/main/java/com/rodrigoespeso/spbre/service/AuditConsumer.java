package com.rodrigoespeso.spbre.service;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
public class AuditConsumer {

    @KafkaListener(topics = "transfer-audit", groupId = "audit-group")
    public void consumeMessage(String message) {
        // Aquí podrías almacenar el mensaje en la base de datos de auditoría
        System.out.println("Audit Log: " + message);
    }
}
