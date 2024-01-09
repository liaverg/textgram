package com.liaverg.textgram.app.usecases.users.application.services;

import com.liaverg.textgram.app.usecases.users.application.ports.out.LoadUserPort;
import com.liaverg.textgram.app.usecases.users.domain.commands.LoginCommand;
import com.liaverg.textgram.app.usecases.users.domain.ids.UserId;
import com.liaverg.textgram.app.usecases.users.domain.views.UserDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class LoginServiceTest {
    private LoginService loginService;
    private LoadUserPort loadUserPortMock;

    @BeforeEach
    void setup(){
        loadUserPortMock = mock(LoadUserPort.class);
        loginService = new LoginService(loadUserPortMock);
    }

    @Test
    @DisplayName("Successful Login")
    public void should_login_user_when_happy_day_scenario(){
        LoginCommand command = new LoginCommand("username@gmail.com", "User1234!");
        UserDTO savedUser = new UserDTO(1, "username@gmail.com", "User1234!", "FREE");
        when(loadUserPortMock.loadUserByUsername(any())).thenReturn(savedUser);

        UserId user_id = loginService.login(command);

        assertEquals(1, user_id.getId());
        verify(loadUserPortMock).loadUserByUsername("username@gmail.com");
    }

    @Test
    @DisplayName("Failed Login When Username Does not Exist")
    public void should_fail_to_login_user_when_load_fails(){
        LoginCommand command = new LoginCommand("username@gmail.com", "User1234!");
        when(loadUserPortMock.loadUserByUsername(any())).thenReturn(null);

        UserId user_id = loginService.login(command);

        assertNull(user_id);
        verify(loadUserPortMock).loadUserByUsername("username@gmail.com");
    }

    @Test
    @DisplayName("Failed Login When Password Does Not Match")
    public void should_fail_to_login_user_when_password_does_not_match(){
        LoginCommand command = new LoginCommand("username@gmail.com", "User1234!");
        UserDTO savedUser = new UserDTO(1, "username@gmail.com", "User0000!", "FREE");
        when(loadUserPortMock.loadUserByUsername(any())).thenReturn(savedUser);

        UserId user_id = loginService.login(command);

        assertEquals(-1, user_id.getId());
        verify(loadUserPortMock).loadUserByUsername("username@gmail.com");
    }
}