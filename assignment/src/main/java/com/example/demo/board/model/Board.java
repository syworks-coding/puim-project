package com.example.demo.board.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;

import java.time.LocalDateTime;

// @Entity
public class Board {

    @Id
    long boardSeq;
    //User userSeq;

    String subject;
    String content;
    LocalDateTime SubmitDate;
    LocalDateTime ModifyDate;
}
