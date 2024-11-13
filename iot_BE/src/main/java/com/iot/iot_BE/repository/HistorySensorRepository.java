package com.iot.iot_BE.repository;

import com.iot.iot_BE.model.HistorySensor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface HistorySensorRepository extends JpaRepository<HistorySensor, Long> {
    List<HistorySensor> findByTemperature(Double temperature);

    List<HistorySensor> findByHumidity(Double humidity);

    List<HistorySensor> findByLight(Double light);

    List<HistorySensor> findByCreatedAt(LocalDateTime createdAt);

//    @Query(value = "SELECT c FROM HistorySensor c " +
//            " WHERE (:temperature IS NULL OR c.temperature = :temperature) " +
//            " AND (:humidity IS NULL OR c.humidity = :humidity) " +
//            " AND (:light IS NULL OR c.light = :light) " +
//            " AND (:dust IS NULL OR c.dust = :dust)"
//    )
//    List<HistorySensor> search(Double temperature,
//                               Double humidity,
//                               Double light,
//                               Double dust);
//
//
//    @Query("SELECT c FROM HistorySensor c WHERE " +
//            "CAST(c.temperature AS string) LIKE CONCAT('%', :input, '%') " +
//            "OR CAST(c.humidity AS string) LIKE CONCAT('%', :input, '%') " +
//            "OR CAST(c.light AS string) LIKE CONCAT('%', :input, '%') " +
//            "OR CAST(c.dust AS string) LIKE CONCAT('%', :input, '%') " +
//            "OR CONCAT(FUNCTION('DATE_FORMAT', c.createdAt, '%d-%m-%Y %H:%i:%s')) LIKE CONCAT('%', :input, '%')"
//    )
//    List<HistorySensor> searchHistorySensor(@Param("input") String input);


    @Query(value = "SELECT c FROM HistorySensor c " +
            "WHERE (:temperature IS NULL OR c.temperature = :temperature) " +
            "AND (:humidity IS NULL OR c.humidity = :humidity) " +
            "AND (:light IS NULL OR c.light = :light) " +
            "AND (:dust IS NULL OR c.dust = :dust) " +
            "AND (:input IS NULL OR (" +
            "CAST(c.temperature AS string) LIKE CONCAT('%', :input, '%') " +
            "OR CAST(c.humidity AS string) LIKE CONCAT('%', :input, '%') " +
            "OR CAST(c.light AS string) LIKE CONCAT('%', :input, '%') " +
            "OR CAST(c.dust AS string) LIKE CONCAT('%', :input, '%') " +
            "OR CAST(DATE_FORMAT(c.createdAt, '%d-%m-%Y %H:%i:%s') AS string) LIKE CONCAT('%', :input, '%')" +
            "))"
    )
    Page<HistorySensor> searchHistorySensor(
            @Param("temperature") Double temperature,
            @Param("humidity") Double humidity,
            @Param("light") Double light,
            @Param("dust") Double dust,
            @Param("input") String input,
            Pageable pageable
    );




}
