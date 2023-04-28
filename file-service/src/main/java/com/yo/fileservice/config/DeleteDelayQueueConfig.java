package com.yo.fileservice.config;

import org.springframework.amqp.core.*;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class DeleteDelayQueueConfig {

    /**延迟队列名*/
    private static String DELETE_DELAY_QUEUE = "delete.file.delay.queue";
    /**延迟队列交换机名*/
    private static String DELETE_DELAY_EXCHANGE = "delete.file.delay.exchange";
    /**处理消息的队列*/
    private static String DELETE_PROCESS_QUEUE = "delete.file.process.queue";
    /**延迟时间*/
    private static int DELETE_DELAY_EXPIRATION = 10000;

    /**创建延迟队列*/
    @Bean
    public Queue delayQueue(){
        return QueueBuilder.durable(DELETE_DELAY_QUEUE)
                .withArgument("x-dead-letter-exchange", DELETE_DELAY_EXCHANGE)
                .withArgument("x-dead-letter-routing-key", DELETE_PROCESS_QUEUE)
                .withArgument("x-message-ttl", DELETE_DELAY_EXPIRATION)
                .build();
    }

    @Bean
    public Queue processQueue(){
        return QueueBuilder.durable(DELETE_PROCESS_QUEUE)
                .build();
    }

    @Bean
    public DirectExchange delayExchange(){
        return new DirectExchange(DELETE_DELAY_EXCHANGE);
    }

    @Bean
    Binding dlxBinding(@Qualifier(value = "delayExchange") DirectExchange directExchange, Queue processQueue){
        return BindingBuilder.bind(processQueue)
                .to(directExchange)
                .with(DELETE_PROCESS_QUEUE);
                //绑定，以DELETE_PROCESS_QUEUE为routing key
    }

}
