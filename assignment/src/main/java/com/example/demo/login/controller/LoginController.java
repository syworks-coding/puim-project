package com.example.demo.login.controller;

import com.example.demo.login.service.LoginService;
import com.example.demo.user.dto.UserCreateDTO;
import com.example.demo.user.dto.UserLoginDTO;
import com.example.demo.user.model.User;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
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

    @Operation(
            summary = "로그인 페이지",
            description = "로그인 페이지를 표시합니다. 이미 로그인된 경우, 이전 페이지로 리다이렉트 됩니다.",
            security = @SecurityRequirement(name = "bearerAuth") // 필요한 경우 인증 요구 사항 추가
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "로그인 페이지 조회 성공"),
            @ApiResponse(responseCode = "302", description = "로그인된 경우 리다이렉트")
    })
    @GetMapping("/login")
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

    @Operation(
            summary = "로그아웃",
            description = "사용자를 로그아웃 시키고 세션을 초기화합니다. 로그아웃 후 이전 페이지로 리다이렉트하거나 홈 페이지로 리다이렉트됩니다.",
            security = @SecurityRequirement(name = "bearerAuth")
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "로그아웃 성공"),
            @ApiResponse(responseCode = "403", description = "잘못된 접근")
    })
    @GetMapping("/logout")
    public String logout(HttpSession session,
                         @RequestHeader(value = "Referer", required = false) String referer)  {
        session.setAttribute("SPRING_SECURITY_CONTEXT", null);

        return "redirect:" + (referer == null ? "/" : referer);
    }
}
