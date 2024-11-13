package com.iot.iot_BE.service;

import com.iot.iot_BE.model.HistoryAction;
import com.iot.iot_BE.model.HistorySensor;
import com.iot.iot_BE.repository.HistoryActionRepository;
import com.iot.iot_BE.repository.HistorySensorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Arrays;
import java.util.List;


@Service
public class HistorySensorService {
    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");;
    @Autowired
    private HistorySensorRepository historySensorRepository;

    public List<HistorySensor> getAllHistorySensor() {
        return historySensorRepository.findAll();
    }

//    public List<HistorySensor> searchHistorySensor(String input) {
//        if (input == null || input.trim().isEmpty()) {
//            return historySensorRepository.findAll();
//        }
//        return  historySensorRepository.searchHistorySensor(input);
//    }
    public Page<HistorySensor> searchHistorySensorByType(Double temperature, Double humidity, Double light, Double dust, String query, String sortField, String sortDirection, int pageNumber, int pageSize) {
        // Tạo Pageable từ các tham số
        Sort.Direction direction = Sort.Direction.fromString(sortDirection);
        Pageable pageable = PageRequest.of(pageNumber, pageSize, Sort.by(direction, sortField));

        // Gọi repository với pageable
        Page<HistorySensor> resultPage = historySensorRepository.searchHistorySensor(temperature, humidity, light, dust, query, pageable);

        // Trả về danh sách các phần tử trong trang
        return resultPage;
    }

//    public List<HistorySensor> getSensor(String sortField, String sortDirection) {
//
//        List<String> validFields = Arrays.asList("id", "temperature", "humidity", "light", "dust");
//
//        if (!validFields.contains(sortField)) {
//            throw new IllegalArgumentException("Invalid sort field: " + sortField);
//        }
//        Sort.Direction direction = sortDirection.equalsIgnoreCase("DESC") ? Sort.Direction.DESC : Sort.Direction.ASC;
//        Sort sort = Sort.by(direction, sortField);
//
//        return historySensorRepository.findAll(sort);
//    }

    public Page<HistorySensor> getSensor(String sortField, String sortDirection, int pageNumber, int pageSize) {

        List<String> validFields = Arrays.asList("id", "temperature", "humidity", "light", "dust");

        if (!validFields.contains(sortField)) {
            throw new IllegalArgumentException("Invalid sort field: " + sortField);
        }
        Sort.Direction direction = sortDirection.equalsIgnoreCase("DESC") ? Sort.Direction.DESC : Sort.Direction.ASC;
        Sort sort = Sort.by(direction, sortField);

        Pageable pageable = PageRequest.of(pageNumber, pageSize, sort);

        return historySensorRepository.findAll(pageable);
    }



}
