package com.liaverg.textgram.app.usecases.users.adapters.in.web;

import com.liaverg.textgram.app.usecases.users.application.services.RegisterService;
import com.liaverg.textgram.app.usecases.users.domain.commands.RegisterCommand;
import io.javalin.http.HttpStatus;
import io.javalin.http.Context;
import io.javalin.openapi.*;
import jakarta.validation.ConstraintViolationException;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class RegisterController {
    private final RegisterService registerService;

    @OpenApi(
            summary = "Register user",
            operationId = "register",
            path = "users/register",
            methods = HttpMethod.POST,
            tags = {"Users"},
            requestBody = @OpenApiRequestBody(content = {@OpenApiContent(from = RegisterCommand.class)}),
            responses = {
                    @OpenApiResponse(status = "201",
                            description = "Successful registration",
                            content = @OpenApiContent(from = String.class)),
                    @OpenApiResponse(status = "400",
                            description = "Error message related to unsuccessful registration due to existing user or invalid input format",
                            content = @OpenApiContent(from = String.class))
            }
    )
    public void register(Context context) {
        try {
            RegisterCommand command = context.bodyAsClass(RegisterCommand.class);
            boolean registrationResult = registerService.register(command);
            if (registrationResult) {
                context.status(HttpStatus.CREATED.getCode());
                context.result("User Registered Successfully");
            } else {
                context.status(HttpStatus.BAD_REQUEST.getCode());
                context.result("User Already Exists");
            }
        } catch (Exception e) {
            if (e.getCause() instanceof ConstraintViolationException) {
                context.result(formatConstraintViolationMessage(e.getCause().getMessage()));
            }
            context.status(HttpStatus.BAD_REQUEST.getCode());
        }
    }

    private String formatConstraintViolationMessage(String constraintViolationMessage){
        return constraintViolationMessage.substring(
                constraintViolationMessage.indexOf(":") + 2);
    }
}