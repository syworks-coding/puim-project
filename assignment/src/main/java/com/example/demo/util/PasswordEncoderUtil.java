package com.example.demo.util;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class PasswordEncoderUtil {
    public static void main(String[] args) {
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        System.out.println("12 Encoded password: " + encoder.encode("12"));
        System.out.println("password1 Encoded password: " + encoder.encode("password1"));
        System.out.println("password2 Encoded password: " + encoder.encode("password2"));
        System.out.println("password3 Encoded password: " + encoder.encode("password3"));
        System.out.println("password Encoded password: " + encoder.encode("password"));
    }
}
