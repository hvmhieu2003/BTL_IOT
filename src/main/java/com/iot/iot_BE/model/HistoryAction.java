package com.iot.iot_BE.model;

import jakarta.persistence.*;

@Entity
@Table(name = "history_actions")
public class HistoryAction {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "fan", length = 255)
    private String fan;

    @Column(name = "light", length = 255)
    private String light;

    @Column(name = "ac", length = 255)
    private String ac;
}
