package com.example.demo.comment.service;

import com.example.demo.comment.dto.CommentDTO;
import com.example.demo.comment.dto.CommentQueryDTO;
import com.example.demo.comment.dto.CommentResponseDTO;
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

    public CommentResponseDTO findByPostId(long postId) {
        int totalCount = commentRepository.countByPostId(postId);

        CommentResponseDTO commentResponseDTO = new CommentResponseDTO();
        commentResponseDTO.setComments(getCommentViewDTOS(postId));
        commentResponseDTO.setTotalCount(totalCount);

        return commentResponseDTO;
    }

    private List<CommentViewDTO> getCommentViewDTOS(long postId) {
        List<CommentViewDTO> comments = new ArrayList<>();

        List<CommentQueryDTO> parentComments = commentMapper.findParentCommentsByPostId(postId);

        for (CommentQueryDTO parent : parentComments) {
            CommentViewDTO commentViewDTO = commentQueryDTOToCommentViewDTO(parent);

            commentViewDTO.setReplies(null);

            List<CommentQueryDTO> childComments = commentMapper.findRepliesByParentId(parent.getId());
            List<CommentViewDTO> convertedChildComments = new ArrayList<>();

            for (CommentQueryDTO childComment : childComments) {
                CommentViewDTO converted = commentQueryDTOToCommentViewDTO(childComment);
                convertedChildComments.add(converted);
            }
            commentViewDTO.setReplies(convertedChildComments);
            comments.add(commentViewDTO);
        }
        return comments;
    }

    private CommentViewDTO commentQueryDTOToCommentViewDTO(CommentQueryDTO commentQueryDTO) {
        CommentViewDTO commentViewDTO = new CommentViewDTO();
        commentViewDTO.setPostId(commentQueryDTO.getPostId());
        commentViewDTO.setUsername(commentQueryDTO.getUserName());
        commentViewDTO.setContent(commentQueryDTO.getContent());
        commentViewDTO.setCreatedAt(commentQueryDTO.getCreatedAt());
        return commentViewDTO;
    }

    public void updateComment(long id, CommentDTO commentDTO) {


//        Comment findComment = commentRepository.findById(id).orElseThrow();
//        findComment.setUsername(comment.getUsername());
//        findComment.setContent(comment.getContent());
    }

}
