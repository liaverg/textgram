package com.liaverg.textgram.app.usecases.users.domain.commands;

import jakarta.validation.constraints.NotNull;

import static com.liaverg.textgram.app.constraints.Validator.validate;

public record LoginCommand(
    @NotNull(message = "Username not provided")
    String username,
    @NotNull(message = "Password not provided")
    String password
) {
    public LoginCommand(String username, String password) {
        this.username = username;
        this.password = password;
        validate(this);
    }
}