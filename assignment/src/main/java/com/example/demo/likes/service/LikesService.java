package com.example.demo.likes.service;

import com.example.demo.likes.model.Likes;
import com.example.demo.likes.repository.LikesRepository;
import com.example.demo.post.model.Post;
import com.example.demo.post.repository.PostRepository;
import com.example.demo.user.model.User;
import com.example.demo.user.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class LikesService {

    private final LikesRepository likesRepository;
    private final PostRepository postRepository;
    private final UserRepository userRepository;

    public Integer getLikesCount(long postId) {

        return likesRepository.countByPostId(postId);
    }

    public boolean getIsLiked(long postId, long userId) {

        return likesRepository.findByPostIdAndUserId(postId, userId).isPresent();
    }

    @Transactional // 이걸 여기다 붙여도 되는게 맞나
    public void toggleLikes(long postId, long userId) {

        // 삭제
        Optional<Likes> LikesOptional = likesRepository.findByPostIdAndUserId(postId, userId);
        if(LikesOptional.isPresent()) {
            Likes likes = LikesOptional.get();
            likesRepository.deleteById(likes.getId());

            return;
        }

        // 생성
        Post post = postRepository.findById(postId).orElseThrow();
        User user = userRepository.findById(userId).orElseThrow();

        Likes likes = new Likes();
        likes.setPost(post);
        likes.setUser(user);
        likesRepository.save(likes);
    }

    @Transactional
    public void deleteByPostId(long postId) {
        List<Likes> likesList = likesRepository.findByPostId(postId);
        for (Likes likes : likesList) {
            likesRepository.delete(likes);
        }
    }
}
