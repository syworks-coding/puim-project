package com.example.demo.login.service;

import com.example.demo.user.model.User;
import com.example.demo.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class LoginService {

    private final UserRepository userRepository;

    public User login(String id, String password) {
        Optional<User> findUser = userRepository.findByUserId(id);

        User user = findUser.orElse(null);

        if(user != null && user.getUserPw().equals(password)) {
            return user;
        }

        return null;
    }

}
