package com.example.demo.board.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class BoardController {

    @GetMapping(value = "/")
    public String showBoardList() {
        return "main";
    }

    @GetMapping(value = "/add-post")
    public String showAddPostPage() {
        return "addPost";
    }
}
