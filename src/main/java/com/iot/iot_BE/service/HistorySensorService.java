package com.iot.iot_BE.service;

import com.iot.iot_BE.model.HistoryAction;
import com.iot.iot_BE.model.HistorySensor;
import com.iot.iot_BE.repository.HistoryActionRepository;
import com.iot.iot_BE.repository.HistorySensorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class HistorySensorService {
    @Autowired
    private HistorySensorRepository historySensorRepository;

    public List<HistorySensor> getAllHistorySensor() {
        return historySensorRepository.findAll();
    }

    public List<HistorySensor> searchHistorySensor(String type, String value) {
        switch (type.toLowerCase()) {
            case "temperature":
                return historySensorRepository.findByTemperature(Double.valueOf(value));
            case "humidity":
                return historySensorRepository.findByHumidity(Double.valueOf(value));
            case "light":
                return historySensorRepository.findByLight(Double.valueOf(value));
            case "createdat":
                return historySensorRepository.findByCreatedAt(LocalDateTime.parse(value));
            default:
                throw new IllegalArgumentException("Invalid type: " + type);
        }
    }

}
