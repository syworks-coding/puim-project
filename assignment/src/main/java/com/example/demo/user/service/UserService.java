package com.example.demo.user.service;

import com.example.demo.user.dto.UserDTO;
import com.example.demo.user.model.User;
import com.example.demo.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    public User saveUser(UserDTO userDTO) {
        User user = new User();

        user.setUserId(userDTO.getUserId());
        user.setUserPw(userDTO.getUserPw());

        return userRepository.save(user);
    }

    public void deleteUser(String id) {
        User user = userRepository.findByUserId(id).orElseThrow();
        userRepository.delete(user);
    }
}
