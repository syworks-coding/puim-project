package com.example.demo.comment.repository;

import com.example.demo.comment.model.Comment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Long> {
    int countByPostId(long postId);
    List<Comment> findByPostId(long postId);
    List<Comment> findByUserId(long userId);
}
