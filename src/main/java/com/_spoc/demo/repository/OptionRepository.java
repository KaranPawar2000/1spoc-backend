package com._spoc.demo.repository;


import com._spoc.demo.entity.FlowOption;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OptionRepository extends JpaRepository<FlowOption, Long> {
}
