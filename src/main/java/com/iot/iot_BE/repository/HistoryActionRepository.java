package com.iot.iot_BE.repository;

import com.iot.iot_BE.model.HistoryAction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface HistoryActionRepository extends JpaRepository<HistoryAction, Long> {
    List<HistoryAction> findByDevice(String device);
    List<HistoryAction> findByAction(String action);
    List<HistoryAction> findByCreatedAt(LocalDateTime createdAt);
}
