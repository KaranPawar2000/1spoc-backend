package com._spoc.demo.service;

import com._spoc.demo.entity.FlowOption;
import com._spoc.demo.entity.FlowStep;
import com._spoc.demo.entity.UserSession;
import com._spoc.demo.repository.SessionRepository;
import com._spoc.demo.service.FlowService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class WhatsAppService {

    private final FlowService flowService;
    private final SessionRepository sessionRepository;

    @Value("${whatsapp.token}")
    private String token;

    @Value("${whatsapp.phone-number-id}")
    private String phoneNumberId;

    private final RestTemplate restTemplate = new RestTemplate();

    // ================= MAIN =================

    public void handleMessage(String from, String message) {

        if (message == null) return;

        message = message.trim().toUpperCase();

        System.out.println("FROM: " + from + " MESSAGE: " + message);

        // 🔥 STEP 1: GET SESSION
        UserSession session = sessionRepository
                .findByPhone(from)
                .orElseGet(() -> {
                    UserSession s = new UserSession();
                    s.setPhone(from);
                    s.setCurrentStep("START");
                    return s;
                });

        // 🔥 STEP 2: GET CURRENT STEP
        FlowStep currentStep = flowService.getStep(session.getCurrentStep());

        if (currentStep == null) {
            sendText(from, "Flow not configured");
            return;
        }

        // 🔥 STEP 3: PROCESS INPUT
        if ("BUTTON".equals(currentStep.getType()) ||
                "LIST".equals(currentStep.getType())) {

            for (FlowOption opt : currentStep.getOptions()) {
                if (opt.getOptionId().equalsIgnoreCase(message)) {
                    session.setCurrentStep(opt.getNextStep());
                }
            }
        }

        else if ("INPUT".equals(currentStep.getType())) {
            session.setCurrentStep(currentStep.getNextStep());
        }

        // 🔥 SAVE SESSION
        sessionRepository.save(session);

        // 🔥 STEP 4: GET NEXT STEP
        FlowStep nextStep = flowService.getStep(session.getCurrentStep());

        if (nextStep == null) {
            sendText(from, "Flow ended");
            return;
        }

        // 🔥 STEP 5: EXECUTE
        executeStep(from, nextStep);
    }

    // ================= EXECUTE =================

    private void executeStep(String to, FlowStep step) {

        switch (step.getType()) {

            case "TEXT":
                sendText(to, step.getMessage());
                break;

            case "INPUT":
                sendText(to, step.getMessage());
                break;

            case "BUTTON":
                sendButtons(to, step);
                break;

            case "LIST":
                sendList(to, step);
                break;
        }
    }

    // ================= BUTTON =================

    private void sendButtons(String to, FlowStep step) {

        StringBuilder buttons = new StringBuilder();

        for (FlowOption opt : step.getOptions()) {
            buttons.append(String.format("""
            {
              "type": "reply",
              "reply": {
                "id": "%s",
                "title": "%s"
              }
            },
            """, opt.getOptionId(), opt.getLabel()));
        }

        String body = """
        {
          "messaging_product": "whatsapp",
          "to": "%s",
          "type": "interactive",
          "interactive": {
            "type": "button",
            "body": {
              "text": "%s"
            },
            "action": {
              "buttons": [%s]
            }
          }
        }
        """.formatted(to, step.getMessage(),
                buttons.substring(0, buttons.length() - 1));

        send(body);
    }

    // ================= LIST =================

    private void sendList(String to, FlowStep step) {

        StringBuilder rows = new StringBuilder();

        for (FlowOption opt : step.getOptions()) {
            rows.append(String.format(
                    "{ \"id\": \"%s\", \"title\": \"%s\" },",
                    opt.getOptionId(),
                    opt.getLabel()
            ));
        }

        String body = """
        {
          "messaging_product": "whatsapp",
          "to": "%s",
          "type": "interactive",
          "interactive": {
            "type": "list",
            "body": {
              "text": "%s"
            },
            "action": {
              "button": "Select",
              "sections": [
                {
                  "title": "Options",
                  "rows": [%s]
                }
              ]
            }
          }
        }
        """.formatted(to, step.getMessage(),
                rows.substring(0, rows.length() - 1));

        send(body);
    }

    // ================= COMMON =================

    private void send(String body) {

        String url = "https://graph.facebook.com/v18.0/" + phoneNumberId + "/messages";

        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(token);
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<String> request = new HttpEntity<>(body, headers);

        restTemplate.postForEntity(url, request, String.class);
    }

    private void sendText(String to, String message) {

        message = message.replace("\"", "\\\"").replace("\n", "\\n");

        String body = String.format(
                "{ \"messaging_product\": \"whatsapp\", \"to\": \"%s\", \"type\": \"text\", \"text\": { \"body\": \"%s\" } }",
                to, message
        );

        send(body);
    }
}
