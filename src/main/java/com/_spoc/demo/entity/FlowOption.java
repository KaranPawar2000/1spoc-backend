package com._spoc.demo.entity;


import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
public class FlowOption {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String optionId;
    private String label;
    private String nextStep;

    @ManyToOne
    @JoinColumn(name = "flow_id")
    private FlowStep flowStep;
}
