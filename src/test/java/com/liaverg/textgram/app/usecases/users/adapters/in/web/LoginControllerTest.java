package com.liaverg.textgram.app.usecases.users.adapters.in.web;

import com.liaverg.textgram.app.usecases.users.application.services.LoginService;
import com.liaverg.textgram.app.usecases.users.domain.commands.LoginCommand;
import io.javalin.Javalin;
import io.javalin.http.HttpStatus;
import io.javalin.json.JavalinJackson;
import io.javalin.testtools.JavalinTest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class LoginControllerTest {
    private Javalin app;
    private static final JavalinJackson javalinJackson = new JavalinJackson();
    private LoginService loginServiceMock;
    private static final long NON_EXISTENT_USERNAME_CODE = -1;
    private static final long WRONG_PASSWORD_CODE = -2;

    @BeforeEach
    void setUp() {
        app = Javalin.create();
        loginServiceMock = mock(LoginService.class);
        LoginController loginController = new LoginController(loginServiceMock);
        app.post("/users/login", loginController::login);
    }

    private static String toJson(LoginCommand command) {
        return javalinJackson.toJsonString(command, LoginCommand.class);
    }

    @Test
    @DisplayName("Successful Login")
    void should_login_user_when_happy_day_scenario() {
        LoginCommand command = new LoginCommand("username@gmail.com", "User1234!");
        when(loginServiceMock.login(any())).thenReturn(Long.valueOf(1));

        JavalinTest.test(app, (server, client) -> {
            try (var response = client.post("/users/login", toJson(command))) {
                assertEquals(HttpStatus.OK.getCode(), response.code());
                assertEquals("text/plain", response.header("content-type"));
                assertEquals("User 1 Logged In Successfully", response.body().string());
            }
        });
    }

    @Test
    @DisplayName("Failed Login when Username Does not Exist")
    void should_fail_to_login_user_when_username_does_not_exist() {
        LoginCommand command = new LoginCommand("username@gmail.com", "User1234!");
        when(loginServiceMock.login(any())).thenReturn(NON_EXISTENT_USERNAME_CODE);

        JavalinTest.test(app, (server, client) -> {
            try (var response = client.post("/users/login", toJson(command))) {
                assertEquals(HttpStatus.BAD_REQUEST.getCode(), response.code());
                assertEquals("text/plain", response.header("content-type"));
                assertEquals("Username Does not Exist", response.body().string());
            }
        });
    }

    @Test
    @DisplayName("Failed Login when Password is Wrong")
    void should_fail_to_login_user_when_password_is_wrong() {
        LoginCommand command = new LoginCommand("username@gmail.com", "User1234!");
        when(loginServiceMock.login(any())).thenReturn(WRONG_PASSWORD_CODE);

        JavalinTest.test(app, (server, client) -> {
            try (var response = client.post("/users/login", toJson(command))) {
                assertEquals(HttpStatus.BAD_REQUEST.getCode(), response.code());
                assertEquals("text/plain", response.header("content-type"));
                assertEquals("Password is Wrong", response.body().string());
            }
        });
    }
}