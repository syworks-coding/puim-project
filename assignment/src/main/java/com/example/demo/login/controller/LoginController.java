package com.example.demo.login.controller;

import com.example.demo.login.service.LoginService;
import com.example.demo.user.dto.UserCreateDTO;
import com.example.demo.user.dto.UserLoginDTO;
import com.example.demo.user.model.User;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.nio.file.AccessDeniedException;

@Controller
@RequiredArgsConstructor
public class LoginController {

    private final LoginService loginService;

    @PostMapping("/login")
    public String tryLogin(@Validated @ModelAttribute UserLoginDTO userLoginDTO,
                           BindingResult bindingResult, HttpSession session) {

        User user = loginService.login(userLoginDTO.getUsername(), userLoginDTO.getPassword());

        if(user == null) {
            bindingResult.reject("globalError", "아이디 또는 비밀번호가 일치하지 않습니다.");
        }

        if(bindingResult.hasErrors()) {
            return "login";
        }

        session.setAttribute("user", user);
        String referer = (String) session.getAttribute("referer");

        return "redirect:" + (referer == null ? "/" : referer);
    }

    @GetMapping(value = "/login")
    public String showLoginPage(HttpSession session, Model model,
                                @RequestHeader(value = "Referer", required = false) String referer) {

        User user = (User) session.getAttribute("user");

        if(user != null) {
            return "redirect:" + referer;
        }

        UserLoginDTO userLoginDTO = new UserLoginDTO();
        model.addAttribute("userLoginDTO", userLoginDTO);
        session.setAttribute("referer", referer);

        return "login";
    }

    @GetMapping("/logout")
    public String logout(HttpSession session,
                         @RequestHeader(value = "Referer", required = false) String referer ) throws AccessDeniedException {

        SecurityContext securityContext = (SecurityContext) session.getAttribute("SPRING_SECURITY_CONTEXT");
        User user = (User) securityContext.getAuthentication().getPrincipal();
        if(user == null) {
            throw new AccessDeniedException("잘못된 접근입니다.");
        }

        session.setAttribute("SPRING_SECURITY_CONTEXT", null);

        return "redirect:" + (referer == null ? "/" : referer);
    }
}
