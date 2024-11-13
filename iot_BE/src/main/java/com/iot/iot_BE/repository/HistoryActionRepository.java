package com.iot.iot_BE.repository;

import com.iot.iot_BE.model.HistoryAction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;


@Repository
public interface HistoryActionRepository extends JpaRepository<HistoryAction, Long> {

    @Query("SELECT a FROM HistoryAction a WHERE " +
            "a.device LIKE CONCAT('%', :input, '%') " +
            "OR a.action LIKE CONCAT('%', :input, '%') " +
            "OR CONCAT(FUNCTION('DATE_FORMAT', a.createdAt, '%d-%m-%Y %H:%i:%s')) LIKE CONCAT('%', :input, '%')")
    Page<HistoryAction> searchHistoryActionByInput(
            @Param("input") String input,
            Pageable pageable
    );

}





