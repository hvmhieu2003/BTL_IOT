package com.iot.iot_BE.controller;

import com.iot.iot_BE.model.HistorySensor;
import com.iot.iot_BE.repository.HistorySensorRepository;
import com.iot.iot_BE.service.HistorySensorService;
import com.iot.iot_BE.service.MqttService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.data.domain.Page;

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

//    @GetMapping("/search")
//    public List<HistorySensor> searchHistorySensor(@RequestParam String query) {
//        return historySensorService.searchHistorySensor(query);
//    }

    @GetMapping("/search/bytype")
    public Page<HistorySensor> searchHistorySensorByType(@RequestParam(required = false) Double temperature,
                                                         @RequestParam(required = false) Double humidity,
                                                         @RequestParam(required = false) Double light,
                                                         @RequestParam(required = false) Double dust,
                                                         @RequestParam(required = false) String query,
                                                         @RequestParam(defaultValue = "id") String sortField,
                                                         @RequestParam(defaultValue = "ASC") String sortDirection,
                                                         @RequestParam(defaultValue = "0") int pageNumber,
                                                         @RequestParam(defaultValue = "10") int pageSize) {
        return historySensorService.searchHistorySensorByType(temperature, humidity, light, dust, query, sortField, sortDirection, pageNumber, pageSize);
    }

    @GetMapping("/sort")
    public Page<HistorySensor> getSensors(
            @RequestParam(defaultValue = "id") String sortField,
            @RequestParam(defaultValue = "ASC") String sortDirection,
            @RequestParam(defaultValue = "0") int pageNumber,
            @RequestParam(defaultValue = "10") int pageSize) {

        return historySensorService.getSensor(sortField, sortDirection, pageNumber, pageSize);
    }


}
