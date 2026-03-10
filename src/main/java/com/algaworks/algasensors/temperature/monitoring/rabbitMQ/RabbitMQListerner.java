package com.algaworks.algasensors.temperature.monitoring.rabbitMQ;

import com.algaworks.algasensors.temperature.monitoring.domain.model.TemperatureLogData;
import com.algaworks.algasensors.temperature.monitoring.domain.service.TemperatureMonitoringService;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

import java.time.Duration;

import static com.algaworks.algasensors.temperature.monitoring.rabbitMQ.RabbitMQCOnfig.QUEUE_NAME;

@Slf4j
@Component
@RequiredArgsConstructor
public class RabbitMQListerner {

    private final TemperatureMonitoringService temperatureMonitoringService;

    @RabbitListener(queues = QUEUE_NAME, concurrency = "2-3")
    @SneakyThrows //sin try catch
    public void handle(@Payload TemperatureLogData temperatureLogData) {

        temperatureMonitoringService.processTemperatureReading(temperatureLogData);

        Thread.sleep(Duration.ofSeconds(5));
    }

}
