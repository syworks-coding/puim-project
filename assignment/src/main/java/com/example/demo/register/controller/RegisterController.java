package com.example.demo.register.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class RegisterController {

    @GetMapping(value = "/join")
    public String showJoinPage() {
        return "join";
    }
}
