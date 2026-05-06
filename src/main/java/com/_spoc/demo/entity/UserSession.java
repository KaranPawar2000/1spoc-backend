package com._spoc.demo.entity;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
public class UserSession {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String phone;
    private String currentStep;

    @Lob
    private String data;
}
