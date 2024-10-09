package com.iot.iot_BE.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;
import lombok.*;

import java.util.Date;

@Entity
@Table(name = "history_sensors")
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
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

    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date createdAt;

    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date updatedAt;

    @PrePersist
    public void handleBeforeCreate() {
        Date now = new Date();
        this.createdAt = now;
        this.updatedAt = now;
    }

    @PreUpdate
    public void handleBeforeUpdate() {
        this.updatedAt = new Date();
    }
}
