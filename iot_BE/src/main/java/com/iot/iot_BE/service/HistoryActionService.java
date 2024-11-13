package com.iot.iot_BE.service;

import com.iot.iot_BE.model.HistoryAction;
import com.iot.iot_BE.model.HistorySensor;
import com.iot.iot_BE.repository.HistoryActionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Arrays;
import java.util.List;

@Service
public class HistoryActionService {
    @Autowired
    private HistoryActionRepository repository;

    // Định dạng ngày giờ chuẩn
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("dd-MM-yyyy");

    /**
     * Lấy toàn bộ các HistoryAction từ cơ sở dữ liệu.
     */
    public List<HistoryAction> getAllHistoryActions() {
        return repository.findAll();
    }


    public Page<HistoryAction> searchHistoryActions(String query, String sortField, String sortDirection, int pageNumber, int pageSize) {

        // Tạo Pageable từ các tham số
        Sort.Direction direction = Sort.Direction.fromString(sortDirection);
        Pageable pageable = PageRequest.of(pageNumber, pageSize, Sort.by(direction, sortField));

        // Gọi repository với pageable
        Page<HistoryAction> resultPage = repository.searchHistoryActionByInput(query, pageable);

        // Trả về danh sách các phần tử trong trang
        return resultPage;
    }


    public Page<HistoryAction> getAction(String sortField, String sortDirection, int pageNumber, int pageSize) {

        List<String> validFields = Arrays.asList("id");

        if (!validFields.contains(sortField)) {
            throw new IllegalArgumentException("Invalid sort field: " + sortField);
        }
        Sort.Direction direction = sortDirection.equalsIgnoreCase("DESC") ? Sort.Direction.DESC : Sort.Direction.ASC;
        Sort sort = Sort.by(direction, sortField);

        Pageable pageable = PageRequest.of(pageNumber, pageSize, sort);

        return repository.findAll(pageable);
    }
}
