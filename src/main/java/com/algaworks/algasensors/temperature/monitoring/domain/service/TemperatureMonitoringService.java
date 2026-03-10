package com.algaworks.algasensors.temperature.monitoring.domain.service;

import com.algaworks.algasensors.temperature.monitoring.domain.model.*;
import com.algaworks.algasensors.temperature.monitoring.domain.repository.SensorMonitoringRepository;
import com.algaworks.algasensors.temperature.monitoring.domain.repository.TemperatureLogRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.OffsetDateTime;

@Service
@RequiredArgsConstructor
@Slf4j
public class TemperatureMonitoringService {

    private final SensorMonitoringRepository sensorMonitoringRepository;
    private final TemperatureLogRepository temperatureLogRepository;

    @Transactional
    public void processTemperatureReading(TemperatureLogData temperatureLogData) {

        sensorMonitoringRepository.findById(new SensorId(temperatureLogData.getSensorId()))
                .ifPresentOrElse(
                        sensor -> handleSensorMonitoring(temperatureLogData, sensor), 
                        () -> logIgnoredTemperature(temperatureLogData));

    }

    private void handleSensorMonitoring(TemperatureLogData temperatureLogData, SensorMonitoring sensor) {
        if(sensor.isEnabled()){
            sensor.setLastTemperature(temperatureLogData.getValue());
            sensor.setUpdateAt(OffsetDateTime.now());
            sensorMonitoringRepository.save(sensor);

            TemperatureLog temperatureLog = TemperatureLog.builder()
                    .id(new TemperatureLogId(temperatureLogData.getId()))
                    .sensorId(new SensorId(temperatureLogData.getSensorId()))
                    .registeredAt(temperatureLogData.getRegisteredAt())
                    .value(temperatureLogData.getValue())
                    .build();

            temperatureLogRepository.save(temperatureLog);
            log.info("Temperatura updated para o sensorId {} Temp {}.",
                    temperatureLogData.getSensorId(),temperatureLogData.getValue());
        } else {
            logIgnoredTemperature(temperatureLogData);
        }
    }

    private void logIgnoredTemperature(TemperatureLogData temperatureLogData) {
        log.info("Temperatura ignorada para o sensorId {} Temp {}. O monitoramento deste sensor está desabilitado.",
                temperatureLogData.getSensorId(),temperatureLogData.getValue());
    }
}
