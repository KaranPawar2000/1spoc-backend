package com._spoc.demo.dto;


import lombok.Data;
import java.util.List;

@Data
public class FlowDTO {

    private String stepKey;
    private String type;
    private String message;
    private String nextStep;
    private List<OptionDTO> options;

    @Data
    public static class OptionDTO {
        private String id;
        private String label;
        private String nextStep;
    }
}
