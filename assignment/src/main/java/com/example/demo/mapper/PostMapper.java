package com.example.demo.mapper;

import com.example.demo.post.dto.PostPreviewDTO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface PostMapper {
    // pageSize: 페이지당 표시 항목 수
    // offset: 조회 페이지 인덱스 * 페이지당 표시 항목 수
    List<PostPreviewDTO> findPostInfoById(int pageSize, int offset);
}
