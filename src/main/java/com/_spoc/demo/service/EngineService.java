package com._spoc.demo.service;


import com._spoc.demo.entity.FlowOption;
import com._spoc.demo.entity.FlowStep;
import com._spoc.demo.entity.UserSession;
import com._spoc.demo.repository.SessionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class EngineService {

    private final FlowService flowService;
    private final SessionRepository sessionRepository;

    public String process(String phone, String input) {

        UserSession session = sessionRepository
                .findByPhone(phone)
                .orElseGet(() -> {
                    UserSession s = new UserSession();
                    s.setPhone(phone);
                    s.setCurrentStep("START");
                    return s;
                });

        FlowStep step = flowService.getStep(session.getCurrentStep());

        if (step == null) return "Flow not configured";

        switch (step.getType()) {

            case "TEXT":
                session.setCurrentStep(step.getNextStep());
                break;

            case "INPUT":
                session.setCurrentStep(step.getNextStep());
                break;

            case "BUTTON":
            case "LIST":
                for (FlowOption opt : step.getOptions()) {
                    if (opt.getOptionId().equalsIgnoreCase(input)) {
                        session.setCurrentStep(opt.getNextStep());
                    }
                }
                break;
        }

        sessionRepository.save(session);

        FlowStep next = flowService.getStep(session.getCurrentStep());
        return next != null ? next.getMessage() : "End";
    }
}