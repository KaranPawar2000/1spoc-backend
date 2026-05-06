package com._spoc.demo.utils;


import java.util.List;
import java.util.Map;

public class JsonUtil {

    public static String extractPhone(Map<String, Object> payload) {
        try {
            List<Map<String, Object>> entry = (List<Map<String, Object>>) payload.get("entry");
            Map<String, Object> changes = ((List<Map<String, Object>>) entry.get(0).get("changes")).get(0);
            Map<String, Object> value = (Map<String, Object>) changes.get("value");

            List<Map<String, Object>> messages =
                    (List<Map<String, Object>>) value.get("messages");

            // ✅ IMPORTANT: ignore status events
            if (messages == null || messages.isEmpty()) {
                return null;
            }

            return (String) messages.get(0).get("from");

        } catch (Exception e) {
            return null;
        }
    }

    public static String extractMessage(Map<String, Object> payload) {
        try {
            Map entry = (Map)((List)payload.get("entry")).get(0);
            Map changes = (Map)((List)entry.get("changes")).get(0);
            Map value = (Map)changes.get("value");

            List messages = (List)value.get("messages");

            // ✅ IMPORTANT: ignore status events
            if (messages == null || messages.isEmpty()) {
                return null;
            }

            Map msg = (Map)messages.get(0);

            System.out.println("FULL MESSAGE OBJECT: " + msg);

            // 🔥 BUTTON / LIST CLICK
            if (msg.containsKey("interactive")) {
                Map interactive = (Map) msg.get("interactive");

                if (interactive.containsKey("button_reply")) {
                    return (String)((Map)interactive.get("button_reply")).get("id");
                }

                if (interactive.containsKey("list_reply")) {
                    return (String)((Map)interactive.get("list_reply")).get("id");
                }
            }

            // TEXT
            if (msg.containsKey("text")) {
                return (String)((Map)msg.get("text")).get("body");
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }
}

