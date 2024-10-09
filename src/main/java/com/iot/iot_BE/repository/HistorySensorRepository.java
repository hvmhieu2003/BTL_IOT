package com.iot.iot_BE.repository;

import com.iot.iot_BE.model.HistorySensor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface HistorySensorRepository extends JpaRepository<HistorySensor, Long> {
}
