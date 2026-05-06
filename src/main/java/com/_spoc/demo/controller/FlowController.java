package com._spoc.demo.controller;

import com._spoc.demo.dto.FlowDTO;
import com._spoc.demo.service.FlowService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/flow")
@RequiredArgsConstructor
@CrossOrigin
public class FlowController {

    private final FlowService flowService;

    @PostMapping("/save")
    public ResponseEntity<String> saveFlow(@RequestBody List<FlowDTO> steps) {
        System.out.println("Request Recived");
        flowService.saveFlow(steps);
        return ResponseEntity.ok("Flow saved successfully");
    }
}