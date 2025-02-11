package com.example.demo.likes.repository;

import com.example.demo.likes.model.Likes;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface LikesRepository extends JpaRepository<Likes, Long> {
    int countByPostId(long postId);
    Optional<Likes> findByPostIdAndUserId(long postId, long userId );
    List<Likes> findByUserId(long userId );
    List<Likes> findByPostId(long postId);
}
