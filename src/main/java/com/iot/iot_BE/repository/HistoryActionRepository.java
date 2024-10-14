package com.iot.iot_BE.repository;

import com.iot.iot_BE.model.HistoryAction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface HistoryActionRepository extends JpaRepository<HistoryAction, Long> {
}
