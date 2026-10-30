package com.diego.lime.controller;

import com.diego.lime.dto.ProviderResponse;
import com.diego.lime.service.ProviderService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/providers")
public class ProviderController {

    private final ProviderService service;

    public ProviderController(ProviderService service) {
        this.service = service;
    }

    @GetMapping
    public List<ProviderResponse> getAllProviders() {
        return service.getAllProviders()
                .stream()
                .map(ProviderResponse::from)
                .toList();
    }
}
