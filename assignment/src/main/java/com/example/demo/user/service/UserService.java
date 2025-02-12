package com.example.demo.user.service;

import com.example.demo.comment.service.CommentService;
import com.example.demo.likes.service.LikesService;
import com.example.demo.post.service.PostService;
import com.example.demo.user.dto.UserCreateDTO;
import com.example.demo.user.dto.UserUpdateDTO;
import com.example.demo.user.model.User;
import com.example.demo.user.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.validation.BindingResult;

@Service
@RequiredArgsConstructor
public class UserService implements UserDetailsService {

    private final UserRepository userRepository;
    private final CommentService commentService;
    private final PostService postService;
    private final LikesService likesService;
    private final BCryptPasswordEncoder passwordEncoder;

    @Transactional
    public User saveUser(UserCreateDTO userCreateDTO) {
        User user = new User();

        String encodedPassword = passwordEncoder.encode(userCreateDTO.getPassword());
        user.setUsername(userCreateDTO.getUsername());
        user.setPassword(encodedPassword);

        return userRepository.save(user);
    }

    public boolean equalsCurrent(long userId, String password) {
        User findUser = userRepository.findById(userId).orElseThrow();
        return passwordEncoder.matches(password, findUser.getPassword());
    }

    @Transactional
    public void updateUser(long userId, UserUpdateDTO userUpdateDTO) {

        User user = userRepository.findById(userId).orElseThrow();
        String encodedPassword = passwordEncoder.encode(userUpdateDTO.getNewPw());

        user.setPassword(encodedPassword);
        userRepository.save(user);
    }

    @Transactional
    public void deleteUserById(long id) {
        likesService.deleteByUserId(id);
        postService.deletePostsByUserId(id);
        commentService.deleteByUserId(id);
        userRepository.deleteById(id);
    }

    public boolean canUseId(String userId) {
        return !userRepository.findByUsername(userId).isPresent();
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found: " + username));
    }
}
