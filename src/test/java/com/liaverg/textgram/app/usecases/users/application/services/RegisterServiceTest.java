package com.liaverg.textgram.app.usecases.users.application.services;

import com.liaverg.textgram.app.usecases.users.application.ports.out.LoadUserPort;
import com.liaverg.textgram.app.usecases.users.domain.User;
import com.liaverg.textgram.app.usecases.users.domain.commands.RegisterCommand;
import com.liaverg.textgram.app.usecases.users.application.ports.out.SaveUserPort;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class RegisterServiceTest {
    private RegisterService registerService;
    private SaveUserPort saveUserPortMock;
    private LoadUserPort loadUserPortMock;

    @BeforeEach
    void setup() {
        saveUserPortMock = mock(SaveUserPort.class);
        loadUserPortMock = mock(LoadUserPort.class);
        registerService = new RegisterService(saveUserPortMock, loadUserPortMock);
    }

    @Test
    @DisplayName("Successful Register")
    public void should_register_user_when_happy_day_scenario() {
        RegisterCommand command = new RegisterCommand("username@gmail.com", "User1234!", "FREE");
        when(loadUserPortMock.loadUserByUsername(any())).thenReturn(null);
        doNothing().when(saveUserPortMock).saveUser(any(),any(),any());

        boolean isRegisterSuccessful = registerService.register(command);

        assertTrue(isRegisterSuccessful);
        verify(loadUserPortMock).loadUserByUsername("username@gmail.com");
        verify(saveUserPortMock).saveUser("username@gmail.com", "User1234!", "FREE");
    }

    @Test
    @DisplayName("Failed Register when User Exists")
    public void should_fail_to_register_user_when_user_exits() {
        RegisterCommand command = new RegisterCommand("username@gmail.com", "User1234!", "FREE");
        when(loadUserPortMock.loadUserByUsername(any())).thenReturn(mock(User.class));
        doNothing().when(saveUserPortMock).saveUser(any(), any(), any());

        boolean isRegisterSuccessful = registerService.register(command);

        assertFalse(isRegisterSuccessful);
        verify(loadUserPortMock).loadUserByUsername("username@gmail.com");
        verify(saveUserPortMock, never()).saveUser("username@gmail.com", "User1234!", "FREE");
    }
}