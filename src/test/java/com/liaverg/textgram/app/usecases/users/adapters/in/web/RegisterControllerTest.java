package com.liaverg.textgram.app.usecases.users.adapters.in.web;

import com.liaverg.textgram.app.usecases.users.application.services.RegisterService;
import com.liaverg.textgram.app.usecases.users.domain.commands.RegisterCommand;
import io.javalin.Javalin;
import io.javalin.http.HttpStatus;
import io.javalin.json.JavalinJackson;
import io.javalin.testtools.JavalinTest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

public class RegisterControllerTest {
    private Javalin app;
    private static final JavalinJackson javalinJackson = new JavalinJackson();
    private RegisterService registerServiceMock;

    @BeforeEach
    void setup(){
        app = Javalin.create();
        registerServiceMock = mock(RegisterService.class);
        RegisterController registerController = new RegisterController(registerServiceMock);
        app.post("/api/users/register", registerController::register);
    }

    private static String toJson(RegisterCommand command) {
        return javalinJackson.toJsonString(command, RegisterCommand.class);
    }

    @Test
    @DisplayName("Successful Register")
    public void should_register_user_when_happy_day_scenario(){
        RegisterCommand command = new RegisterCommand("username@gmail.com", "User1234!", "FREE");
        when(registerServiceMock.register(any())).thenReturn(true);

        JavalinTest.test(app, (server, client) -> {
            try(var response = client.post("/api/users/register", toJson(command))){
                assertEquals(HttpStatus.CREATED.getCode(), response.code());
                assertEquals("User Registered Successfully", response.body().string());
            }
        });
    }

    @Test
    @DisplayName("Failed Register when User Exists")
    public void should_fail_to_register_user_when_user_exists(){
        RegisterCommand command = new RegisterCommand("username@gmail.com", "User1234!", "FREE");
        when(registerServiceMock.register(any())).thenReturn(false);

        JavalinTest.test(app, (server, client) -> {
            try( var response = client.post("/api/users/register", toJson(command))){
                assertEquals(HttpStatus.BAD_REQUEST.getCode(), response.code());
                assertEquals("User Already Exists", response.body().string());
            }
        });
    }

    @Test
    @DisplayName("Failed Register when RegisterCommand Invalid")
    public void should_fail_to_register_user_when_register_command_invalid(){
        String command = "{\"username\":\"username\", \"password\":\"User1234!\", \"role\":\"FREE\"}";
        when(registerServiceMock.register(any())).thenReturn(true);

        JavalinTest.test(app, (server, client) -> {
            try(var response = client.post("/api/users/register", command)){
                assertEquals(HttpStatus.BAD_REQUEST.getCode(), response.code());
                assertEquals("Invalid Command", response.body().string());
            }
        });
    }
}