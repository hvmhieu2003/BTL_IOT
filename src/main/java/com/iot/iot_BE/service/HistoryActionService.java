package com.iot.iot_BE.service;

import com.iot.iot_BE.model.HistoryAction;
import com.iot.iot_BE.repository.HistoryActionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class HistoryActionService {
    @Autowired
    private HistoryActionRepository repository;

    public List<HistoryAction> getAllHistoryActions() {
        return repository.findAll();
    }
}
