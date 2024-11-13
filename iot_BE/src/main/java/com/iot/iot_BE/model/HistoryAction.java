package com.iot.iot_BE.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "history_actions")
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class HistoryAction {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String device; // Tên thiết bị

    private String action; // Trạng thái On/Off

    @JsonFormat(pattern = "dd-MM-yyyy HH:mm:ss") // Định dạng bao gồm giờ, phút, giây
    private LocalDateTime createdAt;

    @PrePersist
    public void handleBeforeCreate() {
        this.createdAt = LocalDateTime.now(); // Thay đổi từ Date thành LocalDateTime
    }
}
