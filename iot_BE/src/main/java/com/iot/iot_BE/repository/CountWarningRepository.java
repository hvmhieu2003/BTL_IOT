package com.iot.iot_BE.repository;

import com.iot.iot_BE.model.CountWarning;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CountWarningRepository extends JpaRepository<CountWarning, Long> {
}
