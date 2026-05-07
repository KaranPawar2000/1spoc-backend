package com._spoc.demo.entity;

import jakarta.persistence.*;
import lombok.Data;
import java.util.List;

@Entity
@Data
public class FlowStep {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String stepKey;
    private String type;
    private String message;
    private String nextStep;

//    @OneToMany(mappedBy = "flowStep", cascade = CascadeType.ALL, orphanRemoval = true)
//    private List<FlowOption> options;

    @OneToMany(
            mappedBy = "flowStep",
            cascade = CascadeType.ALL,
            orphanRemoval = true,
            fetch = FetchType.EAGER
    )
    private List<FlowOption> options;
}
