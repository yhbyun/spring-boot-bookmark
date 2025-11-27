package com.example.bookmark.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
public class HelloController {

    // 1. Text Response
    // Access: http://localhost:8080/hello
    @GetMapping("/hello")
    public String hello() {
        return "Hello World! This is a text response.";
    }

    // 2. JSON Response
    // Access: http://localhost:8080/api/hello
    @GetMapping("/api/hello")
    public Map<String, String> helloJson() {
        Map<String, String> response = new HashMap<>();
        response.put("message", "Hello World!");
        response.put("type", "JSON");
        response.put("status", "success");
        return response;
    }
}
