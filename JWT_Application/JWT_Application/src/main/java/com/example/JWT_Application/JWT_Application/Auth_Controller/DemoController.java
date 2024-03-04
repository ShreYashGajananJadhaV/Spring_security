package com.example.JWT_Application.JWT_Application.Auth_Controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v2")
public class DemoController {

    @GetMapping("/demo-controller")
    public ResponseEntity<String> sayHello(){
        return ResponseEntity.ok("Hello from secured Endpoint");
    }
}
