package com.iot.iot_BE.controller;

import com.iot.iot_BE.model.CountWarning;
import com.iot.iot_BE.service.CountWarningService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/count-warning")
public class CountWarningController {

    @Autowired
    private CountWarningService countWarningService;

    @GetMapping
    public long getDustCount() {
        return countWarningService.getDustCount();
    }
}
