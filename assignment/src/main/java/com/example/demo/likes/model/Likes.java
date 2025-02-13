package com.example.demo.likes.model;

import com.example.demo.comment.model.Comment;
import com.example.demo.post.model.Post;
import com.example.demo.user.model.User;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Check;

@Entity
@Table(uniqueConstraints = {
        @UniqueConstraint(columnNames = {"post_id", "user_id"}),
        @UniqueConstraint(columnNames = {"comment_id", "user_id"}),
})
@Check(constraints = "post_id IS NOT NULL OR comment_id IS NOT NULL")
@Setter
@Getter
public class Likes {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @ManyToOne
    @JoinColumn(name = "post_id", nullable = true)
    private Post post;

    @ManyToOne
    @JoinColumn(name = "comment_id", nullable = true)
    private Comment comment;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;
}
