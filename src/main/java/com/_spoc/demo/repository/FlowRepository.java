package com._spoc.demo.repository;


import com._spoc.demo.entity.FlowStep;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FlowRepository extends JpaRepository<FlowStep, Long> {
    FlowStep findByStepKey(String stepKey);
}
