package com.example.demo.comment.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

@Getter
@Setter
public class CommentResponseDTO {
    private List<CommentViewDTO> comments;
    private int totalCount;
}
