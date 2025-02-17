package com.saran.SecurityDemo;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class DemoController {

    @GetMapping("/")
    public String hello(HttpServletRequest request) {
        return "Hello World  " + request.getSession().getId();
    }
}
