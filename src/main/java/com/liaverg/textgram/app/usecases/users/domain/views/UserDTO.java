package com.liaverg.textgram.app.usecases.users.domain.views;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class UserDTO {
    private long id;
    private String username;
    private String password;
    private String role;
}
