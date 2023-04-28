package com.yo.fileservice.config;

import org.springframework.amqp.core.*;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class DeleteGroupFileDelayQueueConfig {

    /**延迟队列名*/
    private static String DELETE_DELAY_QUEUE = "delete.groupfile.delay.queue";
    /**延迟队列交换机名*/
    private static String DELETE_DELAY_EXCHANGE = "delete.groupfile.delay.exchange";
    /**处理消息的队列*/
    private static String DELETE_PROCESS_QUEUE = "delete.groupfile.process.queue";
    /**延迟时间*/
    private static int DELETE_DELAY_EXPIRATION = 10000;

    /**创建延迟队列*/
    @Bean
    public Queue groupDelayQueue(){
        return QueueBuilder.durable(DELETE_DELAY_QUEUE)
                .withArgument("x-dead-letter-exchange", DELETE_DELAY_EXCHANGE)
                .withArgument("x-dead-letter-routing-key", DELETE_PROCESS_QUEUE)
                .withArgument("x-message-ttl", DELETE_DELAY_EXPIRATION)
                .build();
    }

    @Bean
    public Queue groupProcessQueue(){
        return QueueBuilder.durable(DELETE_PROCESS_QUEUE)
                .build();
    }

    @Bean
    public DirectExchange groupDelayExchange(){
        return new DirectExchange(DELETE_DELAY_EXCHANGE);
    }

    @Bean
    Binding groupDlxBinding(@Qualifier(value = "groupDelayExchange") DirectExchange groupDirectExchange, Queue processQueue){
        return BindingBuilder.bind(processQueue)
                .to(groupDirectExchange)
                .with(DELETE_PROCESS_QUEUE);
                //绑定，以DELETE_PROCESS_QUEUE为routing key
    }

}
