package com.example.demo.login.controller;

import com.example.demo.login.service.LoginService;
import com.example.demo.user.dto.UserDTO;
import com.example.demo.user.model.User;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.nio.file.AccessDeniedException;

@Controller
@RequiredArgsConstructor
public class LoginController {

    private final LoginService loginService;

    @PostMapping("/login")
    public ResponseEntity<Void> tryLogin(@Validated @RequestBody UserDTO userDTO, HttpSession session) {

        User user = loginService.login(userDTO.getUserId(), userDTO.getUserPw());

        if(user == null) {
            return ResponseEntity.notFound().build();
        }

        session.setAttribute("user", user);

        return ResponseEntity.ok().build();
    }

    @GetMapping(value = "/login")
    public String showLoginPage(HttpSession session, Model model) {

        User user = (User) session.getAttribute("user");

        model.addAttribute("user", user);

        return "login";
    }

    @GetMapping("/logout")
    public String logout(HttpSession session, HttpServletRequest request) throws AccessDeniedException {

        String currentUrl = request.getRequestURL().toString();
        User user = (User) session.getAttribute("user");
        if(user == null) {
            throw new AccessDeniedException("잘못된 접근입니다.");
        }

        session.setAttribute("user", null);

        return "redirect:" + currentUrl;
    }
}
