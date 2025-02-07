package com.example.demo.post.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

@Getter
@Setter
@ToString
public class PostListDTO { // 게시물 목록 조회 결과

    private long totalCount;

    private List<PostPreviewDTO> posts;

    private long totalPage;
}
