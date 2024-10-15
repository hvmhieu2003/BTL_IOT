package com.iot.iot_BE.repository;

import com.iot.iot_BE.model.HistorySensor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface HistorySensorRepository extends JpaRepository<HistorySensor, Long> {
    List<HistorySensor> findByTemperature(Double temperature);
    List<HistorySensor> findByHumidity(Double humidity);
    List<HistorySensor> findByLight(Double light);
    List<HistorySensor> findByCreatedAt(LocalDateTime createdAt);
}
