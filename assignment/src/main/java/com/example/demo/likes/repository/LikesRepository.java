package com.example.demo.likes.repository;

import com.example.demo.likes.model.Likes;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LikesRepository extends JpaRepository<Likes, Long> {
    int countByPostId(long postId);
}
