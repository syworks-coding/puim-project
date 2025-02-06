package com.example.demo.user.service;

import com.example.demo.comment.service.CommentService;
import com.example.demo.post.service.PostService;
import com.example.demo.user.dto.UserCreateDTO;
import com.example.demo.user.dto.UserUpdateDTO;
import com.example.demo.user.model.User;
import com.example.demo.user.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final CommentService commentService;
    private final PostService postService;

    public User saveUser(UserCreateDTO userCreateDTO) {
        User user = new User();

        user.setUserId(userCreateDTO.getUserId());
        user.setUserPw(userCreateDTO.getUserPw());

        return userRepository.save(user);
    }

    public String updateUser(long id, UserUpdateDTO userUpdateDTO) {
        User user = userRepository.findById(id).orElseThrow();

        if(userUpdateDTO.getOldPw().equals(user.getUserPw())) {

        }

        user.setUserPw(userUpdateDTO.getNewPw());
        userRepository.save(user);
        return null;
    }

    @Transactional
    public void deleteUserById(long id) {
        postService.deletePostsByUserId(id);
        commentService.deleteByUserId(id);
        userRepository.deleteById(id);
    }
}
