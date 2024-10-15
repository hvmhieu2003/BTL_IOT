package com.iot.iot_BE.service;

import com.iot.iot_BE.model.HistoryAction;
import com.iot.iot_BE.repository.HistoryActionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class HistoryActionService {
    @Autowired
    private HistoryActionRepository repository;

    public List<HistoryAction> getAllHistoryActions() {
        return repository.findAll();
    }

    public List<HistoryAction> searchHistoryActions(String type, String value) {
        switch (type.toLowerCase()) {
            case "device":
                return repository.findByDevice(value);
            case "action":
                return repository.findByAction(value);
            case "createdat":
                return repository.findByCreatedAt(LocalDateTime.parse(value));
            default:
                throw new IllegalArgumentException("Invalid type: " + type);
        }
    }
}
