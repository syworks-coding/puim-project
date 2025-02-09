package com.example.demo.login.service;

import com.example.demo.user.model.User;
import com.example.demo.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class LoginService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public User login(String id, String password) {
        Optional<User> findUser = userRepository.findByUsername(id);
        String encodedPassword = passwordEncoder.encode(password);

        User user = findUser.orElse(null);

        if(user != null && user.getPassword().equals(encodedPassword)) {
            return user;
        }

        return null;
    }

}
