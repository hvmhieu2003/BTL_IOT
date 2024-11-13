package com.iot.iot_BE.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "history_sensors")
public class HistorySensor {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "temperature", nullable = true, columnDefinition = "double")
    private Double temperature;

    @Column(name = "humidity", nullable = true, columnDefinition = "double")
    private Double humidity;

    @Column(name = "light", nullable = true, columnDefinition = "double")
    private Double light;

    @Column(name = "dust", nullable = true, columnDefinition = "double")
    private Double dust;

    @JsonFormat(pattern = "dd-MM-yyyy HH:mm:ss") // Định dạng bao gồm giờ, phút, giây
    private LocalDateTime createdAt;

    @PreUpdate
    public void handleBeforeUpdate() {
        // Logic to handle before update (nếu cần)
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Double getTemperature() {
        return temperature;
    }

    public void setTemperature(Double temperature) {
        this.temperature = temperature;
    }

    public Double getHumidity() {
        return humidity;
    }

    public void setHumidity(Double humidity) {
        this.humidity = humidity;
    }

    public Double getLight() {
        return light;
    }

    // Các getter và setter
    public Double getDust() {
        return dust;
    }

    public void setDust(Double dust) {
        this.dust = dust;
    }
    public void setLight(Double light) {
        this.light = light;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    @PrePersist
    public void handleBeforeCreate() {
        this.createdAt = LocalDateTime.now(); // Sử dụng LocalDateTime để lưu thời gian hiện tại
    }
}
