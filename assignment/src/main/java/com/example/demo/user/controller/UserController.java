package com.example.demo.user.controller;

import com.example.demo.user.dto.UserCreateDTO;
import com.example.demo.user.dto.UserUpdateDTO;
import com.example.demo.user.model.User;
import com.example.demo.user.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    // 회원 추가
    @Operation(
            summary = "회원가입 페이지 조회",
            description = "회원가입 화면을 렌더링합니다. 이미 로그인한 사용자는 홈 화면('/')으로 리디렉션됩니다."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "회원가입 페이지 정상 반환"),
            @ApiResponse(responseCode = "302", description = "이미 로그인된 경우 홈으로 리디렉션")
    })
    @GetMapping("/join")
    public String showJoinPage(HttpSession session, Model model) {

        User user = (User) session.getAttribute("user");
        if(user != null) {
            return "redirect:/";
        }

        UserCreateDTO userCreateDTO = new UserCreateDTO();
        model.addAttribute(userCreateDTO);

        return "join";
    }

    @Operation(
            summary = "회원 가입",
            description = "사용자가 입력한 정보로 회원을 생성합니다. 입력값이 유효하지 않거나, 아이디 중복/비밀번호 불일치가 발생하면 다시 회원가입 페이지로 돌아갑니다."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "302", description = "회원가입 성공 후 홈('/')으로 리디렉션"),
            @ApiResponse(responseCode = "400", description = "입력값 검증 실패 - 회원가입 페이지 다시 표시")
    })
    @PostMapping("/users")
    public String createUser(@Validated @ModelAttribute UserCreateDTO userCreateDTO, BindingResult bindingResult) {

        // 중복 회원 검증
        if(!userService.canUseId(userCreateDTO.getUsername())) {
            bindingResult.rejectValue("username", "", "사용할 수 없는 아이디 입니다.");
        }

        // 비밀번호 확인 불일치
        if(!userCreateDTO.passwordMatch()) {
            bindingResult.rejectValue("passwordCheck", "", "새 비밀번호와 일치하지 않습니다.");
        }

        // 유효성 체크
        if(bindingResult.hasErrors()) {
            return "join";
        }

        userService.saveUser(userCreateDTO);
        return "redirect:/";
    }

    // 회원 수정
    @Operation(
            summary = "비밀번호 변경 페이지 조회",
            description = "현재 로그인한 사용자의 비밀번호 변경 페이지를 렌더링합니다. 접근 가능한 권한: ADMIN, USER",
            security = @SecurityRequirement(name = "bearerAuth")
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "비밀번호 변경 페이지 정상 반환"),
            @ApiResponse(responseCode = "403", description = "권한 없음"),
            @ApiResponse(responseCode = "404", description = "사용자를 찾을 수 없음")
    })
    @GetMapping("/users/change-password")
    @PreAuthorize("isAuthenticated()")
    public String showEditUserPage(@AuthenticationPrincipal User user, Model model) {
        if(user == null) {
            throw new AccessDeniedException("권한이 없습니다");
        }

        UserUpdateDTO userUpdateDTO = new UserUpdateDTO();

        model.addAttribute("user", user);
        model.addAttribute("userUpdateDTO", userUpdateDTO);

        return "edit-user";
    }

    @Operation(
            summary = "사용자 비밀번호 변경",
            description = "사용자가 현재 비밀번호와 새 비밀번호를 입력하여 비밀번호를 변경합니다. 권한: ADMIN, USER",
            security = @SecurityRequirement(name = "bearerAuth")
    )
    @ApiResponses({
            @ApiResponse(responseCode = "302", description = "비밀번호 변경 성공 후 설정 페이지로 리디렉션"),
            @ApiResponse(responseCode = "400", description = "비밀번호 확인 불일치 또는 유효성 오류"),
            @ApiResponse(responseCode = "403", description = "권한 없음"),
            @ApiResponse(responseCode = "404", description = "사용자를 찾을 수 없음")
    })
    @PostMapping("/users/edit")
    @PreAuthorize("isAuthenticated()")
    public String editUser(@Validated @ModelAttribute UserUpdateDTO userUpdateDTO,
                           BindingResult bindingResult,
                           @AuthenticationPrincipal User user) {
        if(user == null) {
            throw new AccessDeniedException("권한이 없습니다");
        }

        // 비밀번호 확인 불일치
        if(!userService.equalsCurrent(user.getId(), userUpdateDTO.getOldPw())) {
            bindingResult.rejectValue("oldPw", "", "현재 비밀번호와 일치하지 않습니다.");
        } else if (!userUpdateDTO.idPasswordMatch()) {
            bindingResult.rejectValue("newPwCheck", "", "새 비밀번호와 일치하지 않습니다.");
        } else if(userService.equalsCurrent(user.getId(), userUpdateDTO.getNewPw())) {
            bindingResult.rejectValue("newPw", "", "현재와 동일한 비밀번호로는 변경할 수 없습니다.");
        }

        // 유효성 체크
        if(bindingResult.hasErrors()) {
            return "edit-user";
        }

        userService.updateUser(user.getId(), userUpdateDTO);

        return "redirect:/users/settings";
    }

    // 회원 삭제
    @Operation(
            summary = "회원 탈퇴",
            description = "로그인한 사용자가 회원 탈퇴를 수행합니다. 탈퇴 후에는 로그아웃이 이루어지고, 홈 페이지로 리디렉션됩니다.",
            security = @SecurityRequirement(name = "bearerAuth")
    )
    @ApiResponses({
            @ApiResponse(responseCode = "302", description = "회원 탈퇴 후 홈 페이지로 리디렉션"),
            @ApiResponse(responseCode = "404", description = "사용자를 찾을 수 없음"),
            @ApiResponse(responseCode = "403", description = "권한 없음")
    })
    @GetMapping("/users/delete")
    @PreAuthorize("isAuthenticated()")
    public String deleteUser(HttpSession session,
                             RedirectAttributes redirectAttributes,
                             HttpServletRequest request,
                             HttpServletResponse response) {

        SecurityContext securityContext = (SecurityContext) session.getAttribute("SPRING_SECURITY_CONTEXT");
        Authentication authentication = securityContext.getAuthentication();
        User user = (User) securityContext.getAuthentication().getPrincipal();

        if (authentication != null) {
            new SecurityContextLogoutHandler().logout(request, response, authentication);
        }

        if(user == null) {
            throw new AccessDeniedException("권한이 없습니다");
        }

        userService.deleteUserById(user.getId());
        redirectAttributes.addFlashAttribute("message", "회원 탈퇴 처리되었습니다.");

        return "redirect:/";
    }

    @Operation(
            summary = "사용자 설정 페이지 조회",
            description = "로그인한 사용자의 설정 페이지를 조회합니다. 권한: ADMIN, USER",
            security = @SecurityRequirement(name = "bearerAuth")
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "사용자 설정 페이지 정상 반환"),
            @ApiResponse(responseCode = "403", description = "권한 없음"),
            @ApiResponse(responseCode = "404", description = "사용자를 찾을 수 없음")
    })
    @GetMapping("/users/settings")
    @PreAuthorize("isAuthenticated()")
    public String showUserSettingsPage(@AuthenticationPrincipal User user, Model model) {
        if(user == null) {
            throw new AccessDeniedException("권한이 없습니다");
        }
        model.addAttribute("user", user);

        return "user-settings";
    }
}
