package com.liaverg.textgram.app.usecases.users.adapters.in.web;

import com.liaverg.textgram.app.usecases.users.application.services.RegisterService;
import com.liaverg.textgram.app.usecases.users.domain.commands.RegisterCommand;
import io.javalin.http.HttpStatus;
import io.javalin.http.Context;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class RegisterController {
    private final RegisterService registerService;

    public void register(Context context) {
        try {
            RegisterCommand command = context.bodyAsClass(RegisterCommand.class);
            boolean registrationResult = registerService.register(command);
            if (registrationResult) {
                context.status(HttpStatus.CREATED.getCode());
                context.json("User Registered Successfully");
            } else {
                context.status(HttpStatus.BAD_REQUEST.getCode());
                context.json("User Already Exists");
            }
        } catch (Exception e) {
            context.status(HttpStatus.BAD_REQUEST.getCode());
            context.json("Invalid Command");
        }
    }
}