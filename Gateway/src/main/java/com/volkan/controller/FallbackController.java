package com.volkan.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/fallback")
public class FallbackController {
    @GetMapping("/auth")
    public String fallbackauth() {
        return "Auth service is not available. Please try again later.";
    }

    @GetMapping("/product")
    public String fallbackuser() {
        return "Product service is not available. Please try again later.";
    }

}
