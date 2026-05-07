package com._spoc.demo.service;


import com._spoc.demo.dto.FlowDTO;
import com._spoc.demo.entity.FlowOption;
import com._spoc.demo.entity.FlowStep;
import com._spoc.demo.repository.FlowRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class FlowService {

    private final FlowRepository flowRepository;

    public void saveFlow(List<FlowDTO> steps) {

        flowRepository.deleteAll();

        for (FlowDTO dto : steps) {

            FlowStep step = new FlowStep();
            step.setStepKey(dto.getStepKey());
            step.setType(dto.getType());
            step.setMessage(dto.getMessage());
            step.setNextStep(dto.getNextStep());

                List<FlowOption> options = new ArrayList<>();

            if (dto.getOptions() != null) {
                for (FlowDTO.OptionDTO opt : dto.getOptions()) {
                    FlowOption option = new FlowOption();
                    option.setOptionId(opt.getId());
                    option.setLabel(opt.getLabel());
                    option.setNextStep(opt.getNextStep());
                    option.setFlowStep(step);
                    options.add(option);
                }
            }

            step.setOptions(options);
            flowRepository.save(step);
        }
    }

    public FlowStep getStep(String stepKey) {


        return flowRepository.findByStepKey(stepKey);
    }
}