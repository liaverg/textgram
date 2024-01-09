package com.liaverg.textgram.app.usecases.users.adapters.in.web;

import com.liaverg.textgram.app.usecases.users.application.services.LoginService;
import com.liaverg.textgram.app.usecases.users.domain.commands.LoginCommand;
import io.javalin.http.Context;
import io.javalin.http.HttpStatus;
import io.javalin.openapi.*;
import jakarta.validation.ConstraintViolationException;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class LoginController {
    private final LoginService loginService;
    private static final long NON_EXISTENT_USERNAME_CODE = -1;
    private static final long WRONG_PASSWORD_CODE = -2;

    @OpenApi(
            summary = "Login user",
            operationId = "login",
            path = "/users/login",
            methods = HttpMethod.POST,
            tags = {"Users"},
            requestBody = @OpenApiRequestBody(content = {@OpenApiContent(from = LoginCommand.class)}),
            responses = {
                    @OpenApiResponse(status = "200",
                            description = "Successful login",
                            content = @OpenApiContent(from = String.class)),
                    @OpenApiResponse(status = "400",
                            description = "Error message related to unsuccessful login due to nonexistent username or wrong password",
                            content = @OpenApiContent(from = String.class))
            }
    )
    public void login(Context context){
        try{
            LoginCommand command = context.bodyAsClass(LoginCommand.class);
            long user_id = loginService.login(command);
            if(user_id == NON_EXISTENT_USERNAME_CODE){
                context.status(HttpStatus.BAD_REQUEST.getCode());
                context.result("Username Does not Exist");
            } else if (user_id == WRONG_PASSWORD_CODE){
                context.status(HttpStatus.BAD_REQUEST.getCode());
                context.result("Password is Wrong");
            } else {
                context.status(HttpStatus.OK.getCode());
                context.result("User " + user_id + " Logged In Successfully");
            }
        } catch (Exception e) {
            if (e.getCause() instanceof ConstraintViolationException)
                context.result(formatConstraintViolationMessage(e.getCause().getMessage()));
            else
                context.result(e.getMessage());
            context.status(HttpStatus.BAD_REQUEST.getCode());
        }
    }

    private String formatConstraintViolationMessage(String constraintViolationMessage){
        return constraintViolationMessage.substring(
                constraintViolationMessage.indexOf(":") + 2);
    }
}
