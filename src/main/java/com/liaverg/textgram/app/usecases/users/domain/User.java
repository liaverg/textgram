package com.liaverg.textgram.app.usecases.users.domain;


import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class User {
    private String username;
    private String password;
    private String role;
}
