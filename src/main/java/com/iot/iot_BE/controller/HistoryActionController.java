package com.iot.iot_BE.controller;

import com.iot.iot_BE.model.HistoryAction;
import com.iot.iot_BE.service.HistoryActionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
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
}
