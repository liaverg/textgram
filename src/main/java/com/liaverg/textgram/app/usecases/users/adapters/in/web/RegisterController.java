package com.liaverg.textgram.app.usecases.users.adapters.in.web;

import com.liaverg.textgram.app.usecases.users.application.services.RegisterService;
import com.liaverg.textgram.app.usecases.users.domain.commands.RegisterCommand;
import lombok.RequiredArgsConstructor;

import io.javalin.http.Context;

@RequiredArgsConstructor
public class RegisterController {
    private final RegisterService registerService;

    public void register(Context context){
        RegisterCommand command = context.bodyAsClass(RegisterCommand.class);
        boolean registrationResult = registerService.register(command);
        if (registrationResult)
            context.json("User Registered Successfully");
        else
            context.json("User Couldn't Register");
    }
}
