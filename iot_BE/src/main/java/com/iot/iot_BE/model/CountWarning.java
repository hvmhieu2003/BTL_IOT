package com.iot.iot_BE.model;


import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "count_warning")
@Data
public class CountWarning {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)

    private Long id;

    private Long count;
}
