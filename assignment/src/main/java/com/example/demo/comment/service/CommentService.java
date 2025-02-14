package com.example.demo.comment.service;

import com.example.demo.comment.dto.CommentDTO;
import com.example.demo.comment.dto.CommentQueryDTO;
import com.example.demo.comment.dto.CommentResponseDTO;
import com.example.demo.comment.dto.CommentViewDTO;
import com.example.demo.comment.model.Comment;
import com.example.demo.comment.repository.CommentRepository;
import com.example.demo.likes.model.Likes;
import com.example.demo.likes.repository.LikesRepository;
import com.example.demo.mapper.CommentMapper;
import com.example.demo.post.model.Post;
import com.example.demo.post.repository.PostRepository;
import com.example.demo.user.model.User;
import com.example.demo.user.repository.UserRepository;
import jakarta.transaction.Transactional;
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
    private final LikesRepository likesRepository;

    @Transactional
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

    public Comment findByCommentId(long commentId) {
        return commentRepository.findById(commentId).orElseThrow();
    }

    public CommentResponseDTO findByPostId(long postId, Long userId) {
        int totalCount = commentRepository.countByPostId(postId);

        CommentResponseDTO commentResponseDTO = new CommentResponseDTO();
        commentResponseDTO.setComments(findCommentViewDTOS(postId, userId));
        commentResponseDTO.setTotalCount(totalCount);

        return commentResponseDTO;
    }

    public List<CommentViewDTO> findCommentViewDTOS(long postId, Long userId) {
        List<CommentViewDTO> comments = new ArrayList<>();

        List<CommentQueryDTO> parentComments = commentMapper.findParentCommentsByPostId(postId);

        for (CommentQueryDTO parent : parentComments) {
            CommentViewDTO commentViewDTO = commentQueryDTOToCommentViewDTO(parent, userId);

            commentViewDTO.setReplies(null);

            List<CommentQueryDTO> childComments = commentMapper.findRepliesByParentId(parent.getId());
            List<CommentViewDTO> convertedChildComments = new ArrayList<>();

            for (CommentQueryDTO childComment : childComments) {
                CommentViewDTO converted = commentQueryDTOToCommentViewDTO(childComment, userId);
                convertedChildComments.add(converted);
            }
            commentViewDTO.setReplies(convertedChildComments);
            comments.add(commentViewDTO);
        }
        return comments;
    }

    private CommentViewDTO commentQueryDTOToCommentViewDTO(CommentQueryDTO commentQueryDTO, Long userId) {
        CommentViewDTO commentViewDTO = new CommentViewDTO();
        commentViewDTO.setId(commentQueryDTO.getId());
        commentViewDTO.setUserId(commentQueryDTO.getUserId());
        commentViewDTO.setPostId(commentQueryDTO.getPostId());
        commentViewDTO.setUsername(commentQueryDTO.getUserName());
        commentViewDTO.setContent(commentQueryDTO.getContent());
        commentViewDTO.setCreatedAt(commentQueryDTO.getCreatedAt());
        commentViewDTO.setUpdatedAt(commentQueryDTO.getUpdatedAt());
        commentViewDTO.setLikes(commentQueryDTO.getLikes());
        return commentViewDTO;
    }

    @Transactional
    public void updateComment(long id, CommentDTO commentDTO) {

        Comment findComment = commentRepository.findById(id).orElseThrow();
        findComment.setContent(commentDTO.getContent());
        commentRepository.save(findComment);
    }

    @Transactional
    public void deleteByPostId(long postId) {
        List<Comment> commentList = commentRepository.findByPostId(postId);
        for (Comment comment : commentList) {
            deleteCommentById(comment.getId());
            commentRepository.deleteById(comment.getId());
        }
    }

    @Transactional
    public void deleteCommentById(long id) {
        List<CommentQueryDTO> childComments = commentMapper.findRepliesByParentId(id);

        for (CommentQueryDTO childComment : childComments) {
            likesRepository.deleteByCommentId(childComment.getId());
            commentRepository.deleteById(childComment.getId());
        }
        likesRepository.deleteByCommentId(id);
        commentRepository.deleteById(id);
    }

    @Transactional
    public void deleteByUserId(long userId) {
        List<Comment> commentList = commentRepository.findByUserId(userId);
        for (Comment comment : commentList) {
            deleteCommentById(comment.getId());
        }
    }
}
