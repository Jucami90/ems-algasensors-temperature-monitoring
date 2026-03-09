package com.algaworks.algasensors.temperature.monitoring.rabbitMQ;

import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


@Configuration
public class RabbitMQCOnfig {

    @Bean
    public RabbitAdmin rabbitAdmin(ConnectionFactory connectionFactory) {
        return new RabbitAdmin(connectionFactory);
    }

    @Bean
    public Queue queue() {
        return QueueBuilder.durable("temperature-monitoring.process-temperature.v1.q").build();
    }

    public FanoutExchange exchange() {
        return ExchangeBuilder.fanoutExchange(
                "temperature-processing.temperature-received.v1.e"
        ).build();
    }

    @Bean
    public Binding binding() {
        return BindingBuilder.bind(queue())
                .to(exchange());
    }
}
