package com.example.demo.user.service;

import com.example.demo.comment.service.CommentService;
import com.example.demo.post.service.PostService;
import com.example.demo.user.dto.UserCreateDTO;
import com.example.demo.user.dto.UserUpdateDTO;
import com.example.demo.user.model.User;
import com.example.demo.user.repository.UserRepository;
import jakarta.transaction.Transactional;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService implements UserDetailsService {

    private final UserRepository userRepository;
    private final CommentService commentService;
    private final PostService postService;
    private final PasswordEncoder passwordEncoder;

    public User saveUser(UserCreateDTO userCreateDTO) {
        User user = new User();

        String encodedPassword = passwordEncoder.encode(userCreateDTO.getUserPw());
        user.setUsername(userCreateDTO.getUserId());
        user.setPassword(encodedPassword);

        return userRepository.save(user);
    }

    public String updateUser(long id, UserUpdateDTO userUpdateDTO) {
        User user = userRepository.findById(id).orElseThrow();
        String encodedPassword = passwordEncoder.encode(userUpdateDTO.getNewPw());

        if(userUpdateDTO.getOldPw().equals(user.getUsername())) {

        }

        user.setPassword(encodedPassword);
        userRepository.save(user);
        return null;
    }

    @Transactional
    public void deleteUserById(long id) {
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
