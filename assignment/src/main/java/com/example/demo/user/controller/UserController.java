package com.example.demo.user.controller;

import com.example.demo.user.dto.UserCreateDTO;
import com.example.demo.user.dto.UserUpdateDTO;
import com.example.demo.user.model.User;
import com.example.demo.user.service.UserService;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.NoSuchElementException;

@Controller
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    // 회원 추가
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

    @PostMapping("/users")
    public String createUser(@Validated @ModelAttribute UserCreateDTO userCreateDTO, BindingResult bindingResult) {

        // 중복 회원 검증
        if(userService.canUseId(userCreateDTO.getUserId())) {
            bindingResult.rejectValue("userId", "", "사용할 수 없는 아이디 입니다.");
        }

        // 비밀번호 확인 불일치
        if(!userCreateDTO.idPasswordMatch()) {
            bindingResult.rejectValue("userPwCheck", "", "새 비밀번호와 일치하지 않습니다.");
        }

        // 유효성 체크
        if(bindingResult.hasErrors()) {
            return "join";
        }

        userService.saveUser(userCreateDTO);
        return "redirect:/";
    }

    // 회원 수정
    @GetMapping("/users/change-password")
    public String showEditUserPage(HttpSession session, Model model) {

        User user = (User) session.getAttribute("user");

        if(user == null) {
            throw new NoSuchElementException();
        }

        UserUpdateDTO userUpdateDTO = new UserUpdateDTO();

        model.addAttribute("user", user);
        model.addAttribute("userUpdateDTO", userUpdateDTO);

        return "edit-user";
    }

    @PostMapping("/users/edit")
    public String editUser(@Validated @ModelAttribute UserUpdateDTO userUpdateDTO,
                           HttpSession session,
                           BindingResult bindingResult) {
        User user = (User) session.getAttribute("user");
        if (user == null) {
            throw new NoSuchElementException();
        }

        // 비밀번호 확인 불일치
        if(!userUpdateDTO.getOldPw().equals(user.getUserPw())) {
            bindingResult.rejectValue("oldPw", "", "현재 비밀번호와 일치하지 않습니다.");
        } else if (!userUpdateDTO.idPasswordMatch()) {
            bindingResult.rejectValue("newPwCheck", "", "새 비밀번호와 일치하지 않습니다.");
        } else if(!userUpdateDTO.idPasswordEqualsCurrent()) {
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
    @GetMapping("/users/delete")
    public String deleteUser(HttpSession session, RedirectAttributes redirectAttributes) {

        User user = (User) session.getAttribute("user");
        if(user == null) {
            throw new NoSuchElementException();
        }

        userService.deleteUserById(user.getId());
        redirectAttributes.addFlashAttribute("message", "회원 탈퇴 처리되었습니다.");

        return "redirect:/";
    }

    @GetMapping("/users/settings")
    public String showUserSettingsPage(HttpSession session, Model model) {

        User user = (User) session.getAttribute("user");
        if(user == null) {
            throw new NoSuchElementException();
        }

        model.addAttribute("user", user);

        return "user-settings";
    }
}
