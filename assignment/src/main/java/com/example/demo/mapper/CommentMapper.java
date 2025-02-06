package com.example.demo.mapper;

import com.example.demo.comment.dto.CommentQueryDTO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface CommentMapper {

    List<CommentQueryDTO> findParentCommentsByPostId(Long postId);

    List<CommentQueryDTO> findRepliesByParentId(Long parentId);
}
