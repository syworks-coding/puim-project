package com.example.demo.comment.service;

import com.example.demo.comment.dto.CommentDTO;
import com.example.demo.comment.dto.CommentQueryDTO;
import com.example.demo.comment.dto.CommentViewDTO;
import com.example.demo.comment.model.Comment;
import com.example.demo.comment.repository.CommentRepository;
import com.example.demo.mapper.CommentMapper;
import com.example.demo.post.model.Post;
import com.example.demo.post.repository.PostRepository;
import com.example.demo.user.model.User;
import com.example.demo.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CommentService {

    private final CommentMapper commentMapper;

    private final CommentRepository commentRepository;
    private final PostRepository postRepository;
    private final UserRepository userRepository;

    public Comment createComment(CommentDTO commentDTO) {

        Post post = postRepository.findById(commentDTO.getPostId()).orElseThrow();
        User user = userRepository.findById(commentDTO.getUserId()).orElseThrow();

        Comment parent = null;
        if(commentDTO.getParentId() != null) {
            parent = commentRepository.findById(commentDTO.getParentId()).orElseThrow();
        }

        Comment comment = new Comment();
        comment.setContent(commentDTO.getContent());
        comment.setPost(post);
        comment.setUser(user);
        comment.setParent(parent);

        return commentRepository.save(comment);
    }

    public List<CommentViewDTO> findByPostId(long postId) {

        List<CommentViewDTO> comments = new ArrayList<>();

        List<CommentQueryDTO> parentComments = commentMapper.findParentCommentsByPostId(postId);

        for (CommentQueryDTO parent : parentComments) {
            CommentViewDTO commentViewDTO = new CommentViewDTO();
            commentViewDTO.setPostId(parent.getPostId());
            commentViewDTO.setUsername(parent.getUserName());
            commentViewDTO.setContent(parent.getContent());
            commentViewDTO.setCreatedAt(parent.getCreatedAt());

            commentViewDTO.setReplies(null);

            List<CommentQueryDTO> childComments = commentMapper.findRepliesByParentId(parent.getId());
            //commentViewDTO.setReplies(convertToDTOList(childComments));

            comments.add(commentViewDTO);
        }

        return comments;
    }

    public void updateComment(long id, CommentDTO commentDTO) {


//        Comment findComment = commentRepository.findById(id).orElseThrow();
//        findComment.setUsername(comment.getUsername());
//        findComment.setContent(comment.getContent());
    }

}
