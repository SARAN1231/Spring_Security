package com.saran.SecurityDemo.Controller;

import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/management/")
public class ManagementController {

    @GetMapping
    public String get() {
        return "GET :: management Controller";
    }

    @PostMapping
    public String post() {
        return "POST :: management Controller";
    }

    @PutMapping
    public String put() {
        return "PUT :: management Controller";
    }

    @DeleteMapping
    public String delete() {
        return "DELETE :: management Controller";
    }
}
