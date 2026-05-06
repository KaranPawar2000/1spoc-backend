package com._spoc.demo.controller;


import com._spoc.demo.service.WhatsAppService;
import com._spoc.demo.utils.JsonUtil;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/webhook")
public class WebhookController {

    @Value("${whatsapp.verify-token}")
    private String verifyToken;

    private final WhatsAppService whatsAppService;

    public WebhookController(WhatsAppService whatsAppService) {
        this.whatsAppService = whatsAppService;
    }

    // ✅ Verification API
    @GetMapping
    public ResponseEntity<String> verifyWebhook(
            @RequestParam("hub.mode") String mode,
            @RequestParam("hub.verify_token") String token,
            @RequestParam("hub.challenge") String challenge) {

        if ("subscribe".equals(mode) && verifyToken.equals(token)) {
            return ResponseEntity.ok(challenge);
        }
        return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
    }

    // ✅ Receive Message
    @PostMapping
    public ResponseEntity<String> receiveMessage(@RequestBody Map<String, Object> payload) {
        System.out.println("WEBHOOK DATA: " + payload);
        String from =
                JsonUtil.extractPhone(payload);
        String message = JsonUtil.extractMessage(payload);

        if (from != null && message != null) {
            whatsAppService.handleMessage(from, message);
        }

        return ResponseEntity.ok("EVENT_RECEIVED");
    }
}

