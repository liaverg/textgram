package com.liaverg.textgram.app.usecases.users.domain.commands;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Pattern;

import static com.liaverg.textgram.app.constraints.Validator.validate;

public record RegisterCommand(
        @Email(message = "Email is Invalid",
                regexp = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$")
        String username,
        @Pattern(message = "Password is Invalid. " +
                "Should contain at least 1 lowercase, 1 uppercase, 1 number, 1 symbol and be 8 characters long",
                regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[!@#$%^&*]).{8,}$")
        String password,
        @Pattern(regexp = "^(free|premium)$")
        String role
) {
    public RegisterCommand(String username, String password, String role) {
        this.username = username;
        this.password = password;
        this.role = role;
        validate(this);
    }
}
