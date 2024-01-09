package com.liaverg.textgram.app.usecases.users.domain.commands;

import jakarta.validation.ConstraintViolationException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class LoginCommandTest {

    @Test
    @DisplayName("Successful LoginCommand")
    public void should_validate_login_command_when_happy_day_scenario(){
        assertDoesNotThrow(() ->
                new LoginCommand("username@gmail.com", "User1234!"));
    }

    @Test
    @DisplayName("Failed LoginCommand when Email Null")
    public void should_fail_to_validate_login_command_when_email_null(){
        ConstraintViolationException exception = assertThrows(ConstraintViolationException.class, () ->
                new LoginCommand(null, "User1234!"));
        assertEquals("username: Username not provided", exception.getMessage());
    }

    @Test
    @DisplayName("Failed LoginCommand when Password Null")
    public void should_fail_to_validate_login_command_when_password_null(){
        ConstraintViolationException exception = assertThrows(ConstraintViolationException.class, () ->
                new LoginCommand("username@gmail.com", null));
        assertEquals("password: Password not provided", exception.getMessage());
    }
}