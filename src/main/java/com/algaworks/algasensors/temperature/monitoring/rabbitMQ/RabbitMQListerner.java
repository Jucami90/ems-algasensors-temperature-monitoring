package com.algaworks.algasensors.temperature.monitoring.rabbitMQ;

import com.algaworks.algasensors.temperature.monitoring.domain.model.TemperatureLogData;
import io.hypersistence.tsid.TSID;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.messaging.handler.annotation.Headers;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.util.Map;

import static com.algaworks.algasensors.temperature.monitoring.rabbitMQ.RabbitMQCOnfig.QUEUE_NAME;

@Slf4j
@Component
@RequiredArgsConstructor
public class RabbitMQListerner {

    @RabbitListener(queues = QUEUE_NAME)
    @SneakyThrows //sin try catch
    public void handle(@Payload TemperatureLogData temperatureLogData,
                       @Headers Map<String, Object> headers) {
        TSID sensorId = temperatureLogData.getSensorId();
        Double temperature = temperatureLogData.getValue();
        log.info("Received temperature log data: sensorId={}, temperature={}",
                sensorId, temperature);
        log.info("Headers: {}", headers);
        Thread.sleep(Duration.ofSeconds(5));
    }

}
