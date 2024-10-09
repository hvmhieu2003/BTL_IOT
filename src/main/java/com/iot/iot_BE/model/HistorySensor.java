package com.iot.iot_BE.model;

import jakarta.persistence.*;
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

}
