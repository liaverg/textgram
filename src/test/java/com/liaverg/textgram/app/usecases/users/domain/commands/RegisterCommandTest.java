package com.liaverg.textgram.app.usecases.users.domain.commands;

import jakarta.validation.ConstraintViolationException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class RegisterCommandTest {

    @Test
    @DisplayName("Successful RegisterCommand")
    public void should_validate_register_command_when_happy_day_scenario(){
        assertDoesNotThrow(() ->
                new RegisterCommand("username@gmail.com", "User1234!", "free"));
    }

    @Test
    @DisplayName("Failed RegisterCommand when Email Invalid")
    public void should_fail_to_validate_register_command_when_email_invalid(){
        ConstraintViolationException exception = assertThrows(ConstraintViolationException.class, () ->
                new RegisterCommand("username@.com", "User1234!", "free"));
        assertEquals("username: Email is Invalid", exception.getMessage());
    }

    @Test
    @DisplayName("Failed RegisterCommand when Password Invalid (no lowercase)")
    public void should_fail_to_validate_register_command_when_password_does_not_contain_lowercase(){
        ConstraintViolationException exception = assertThrows(ConstraintViolationException.class, () ->
                new RegisterCommand("username@gmail.com", "USER1234!", "free"));
        assertEquals("password: Password is Invalid. Should contain at least 1 lowercase, " +
                "1 uppercase, 1 number, 1 symbol and be 8 characters long", exception.getMessage());
    }

    @Test
    @DisplayName("Failed RegisterCommand when Password Invalid (no uppercase)")
    public void should_fail_to_validate_register_command_when_password_does_not_contain_uppercase(){
        ConstraintViolationException exception = assertThrows(ConstraintViolationException.class, () ->
                new RegisterCommand("username@gmail.com", "user1234!", "free"));
        assertEquals("password: Password is Invalid. Should contain at least 1 lowercase, " +
                "1 uppercase, 1 number, 1 symbol and be 8 characters long", exception.getMessage());
    }

    @Test
    @DisplayName("Failed RegisterCommand when Password Invalid (no number)")
    public void should_fail_to_validate_register_command_when_password_does_not_contain_number(){
        ConstraintViolationException exception = assertThrows(ConstraintViolationException.class, () ->
                new RegisterCommand("username@gmail.com", "Username!", "free"));
        assertEquals("password: Password is Invalid. Should contain at least 1 lowercase, " +
                "1 uppercase, 1 number, 1 symbol and be 8 characters long", exception.getMessage());
    }

    @Test
    @DisplayName("Failed RegisterCommand when Password Invalid (no symbol)")
    public void should_fail_to_validate_register_command_when_password_does_not_contain_symbol(){
        ConstraintViolationException exception = assertThrows(ConstraintViolationException.class, () ->
                new RegisterCommand("username@gmail.com", "Username1", "free"));
        assertEquals("password: Password is Invalid. Should contain at least 1 lowercase, " +
                "1 uppercase, 1 number, 1 symbol and be 8 characters long", exception.getMessage());
    }

    @Test
    @DisplayName("Failed RegisterCommand when Password Invalid (too small)")
    public void should_fail_to_validate_register_command_when_password_less_than_8_characters(){
        ConstraintViolationException exception = assertThrows(ConstraintViolationException.class, () ->
                new RegisterCommand("username@gmail.com", "user12!", "free"));
        assertEquals("password: Password is Invalid. Should contain at least 1 lowercase, " +
                "1 uppercase, 1 number, 1 symbol and be 8 characters long", exception.getMessage());
    }

    @Test
    @DisplayName("Failed RegisterCommand when Role Invalid")
    public void should_fail_to_validate_register_command_when_role_invalid(){
        assertThrows(ConstraintViolationException.class, () ->
                new RegisterCommand("username@gmail.com", "User1234!", "vip"));

        ConstraintViolationException exception = assertThrows(ConstraintViolationException.class, () ->
                new RegisterCommand("username@gmail.com", "User1234!", "vip"));
        assertEquals("role: User role is Invalid. Should be Free or Premium", exception.getMessage());
    }
}