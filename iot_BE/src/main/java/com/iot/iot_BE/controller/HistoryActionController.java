package com.iot.iot_BE.controller;

import com.iot.iot_BE.model.HistoryAction;
import com.iot.iot_BE.model.HistorySensor;
import com.iot.iot_BE.service.HistoryActionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/history_actions")
public class HistoryActionController {
    @Autowired
    private HistoryActionService service;

    @GetMapping
    public List<HistoryAction> getHistoryActions() {
        return service.getAllHistoryActions();
    }

    @GetMapping("/search")
    public Page<HistoryAction> searchHistoryActions(@RequestParam String query,
                                                    @RequestParam(defaultValue = "id") String sortField,
                                                    @RequestParam(defaultValue = "ASC") String sortDirection,
                                                    @RequestParam(defaultValue = "0") int pageNumber,
                                                    @RequestParam(defaultValue = "10") int pageSize) {
        return service.searchHistoryActions(query, sortField, sortDirection, pageNumber, pageSize);
    }

    @GetMapping("/sort")
    public Page<HistoryAction> getActions(
            @RequestParam(defaultValue = "id") String sortField,
            @RequestParam(defaultValue = "ASC") String sortDirection,
            @RequestParam(defaultValue = "0") int pageNumber,
            @RequestParam(defaultValue = "10") int pageSize) {

        return service.getAction(sortField, sortDirection, pageNumber, pageSize);
    }

}
