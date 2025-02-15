package com.example.demo.likes.service;

import com.example.demo.comment.model.Comment;
import com.example.demo.comment.repository.CommentRepository;
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
    private final CommentRepository commentRepository;
    private final UserRepository userRepository;

    public Integer getLikesCount(long postId) {

        return likesRepository.countByPostId(postId);
    }

    public boolean getIsLiked(long postId, long userId) {

        return likesRepository.findByPostIdAndUserId(postId, userId).isPresent();
    }

    public Integer getCommentLikesCount(long commentId) {

        return likesRepository.countByCommentId(commentId);
    }

    public boolean getCommentIsLiked(long commentId, long userId) {

        return likesRepository.findByCommentIdAndUserId(commentId, userId).isPresent();
    }

    @Transactional
    public void deleteByUserId(long userId) {
        List<Likes> byUserId = likesRepository.findByUserId(userId);
        for (Likes likes : byUserId) {
            likesRepository.delete(likes);
        }
    }

    @Transactional
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
    public void commentToggleLikes(long commentId, long userId) {

        // 삭제
        Optional<Likes> LikesOptional = likesRepository.findByCommentIdAndUserId(commentId, userId);
        if(LikesOptional.isPresent()) {
            Likes likes = LikesOptional.get();
            likesRepository.deleteById(likes.getId());

            return;
        }

        // 생성
        Comment comment = commentRepository.findById(commentId).orElseThrow();
        User user = userRepository.findById(userId).orElseThrow();

        Likes likes = new Likes();
        likes.setComment(comment);
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
