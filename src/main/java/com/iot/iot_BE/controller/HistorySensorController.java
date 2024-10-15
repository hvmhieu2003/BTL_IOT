package com.iot.iot_BE.controller;

import com.iot.iot_BE.model.HistorySensor;
import com.iot.iot_BE.repository.HistorySensorRepository;
import com.iot.iot_BE.service.HistorySensorService;
import com.iot.iot_BE.service.MqttService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/history_sensor") // Thay đổi đường dẫn cho phù hợp với Dashboard.js
public class HistorySensorController {
    @Autowired
    private HistorySensorService historySensorService;

    @GetMapping
    public List<HistorySensor> getHistorySensor() {
        return historySensorService.getAllHistorySensor();
    }

    @GetMapping("/search")
    public List<HistorySensor> searchHistorySensor(@RequestParam String type, @RequestParam String value) {
        return historySensorService.searchHistorySensor(type, value);
    }

}
