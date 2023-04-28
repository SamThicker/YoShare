package com.yo.fileservice.components;

import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class MessageSender {

    @Autowired
    private AmqpTemplate amqpTemplate;

    public void send(String routingKey, String msg) {
        amqpTemplate.convertAndSend(routingKey, msg);
    }

}
