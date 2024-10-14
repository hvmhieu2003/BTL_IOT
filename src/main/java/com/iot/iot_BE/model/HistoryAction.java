package com.iot.iot_BE.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;
import lombok.*;

import java.util.Date;

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

    @Column(name = "fan", length = 255)
    private String fan;

    @Column(name = "light", length = 255)
    private String light;

    @Column(name = "ac", length = 255)
    private String ac;

    @JsonFormat(pattern = "dd-MM-yyyy")
    private Date createdAt;

    @JsonFormat(pattern = "dd-MM-yyyy")
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
