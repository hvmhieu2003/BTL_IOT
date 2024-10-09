package com.iot.iot_BE.controller;

import com.iot.iot_BE.model.HistorySensor;
import com.iot.iot_BE.repository.HistorySensorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/sensordata")
public class HistorySensorController {
    @Autowired
    private HistorySensorRepository historySensorRepository;

    @PostMapping
    public ResponseEntity<String> saveSensorData(@RequestBody HistorySensor sensorData) {
        historySensorRepository.save(sensorData);
        return ResponseEntity.ok("Sensor data received");
    }

    @GetMapping
    public List<HistorySensor> getAllSensorData() {
        return historySensorRepository.findAll();
    }
}
