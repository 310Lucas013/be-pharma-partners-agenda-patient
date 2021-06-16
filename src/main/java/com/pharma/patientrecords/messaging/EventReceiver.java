package com.pharma.patientrecords.messaging;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

@Component
public class EventReceiver {

    private Logger log = LoggerFactory.getLogger(EventReceiver.class);

    @RabbitListener(queues = "${rabbitmq.queue}")
    public void receive(Message event) {
        System.out.println("received the event!");
        log.info("Received event in service Patient Records: {}", event);
        // Convert to object.
        // Do with it whatever you please.
    }
}

