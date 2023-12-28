package com.liaverg.textgram.appconfig;

import com.liaverg.textgram.app.usecases.users.adapters.in.web.RegisterController;
import com.liaverg.textgram.app.usecases.users.adapters.out.persistence.UserAdapter;
import com.liaverg.textgram.app.usecases.users.application.services.RegisterService;
import lombok.Getter;

@Getter
public class AppConfig {
    private final RegisterController registerController;

    public AppConfig() {
        UserAdapter userAdapter = new UserAdapter();
        RegisterService registerService = new RegisterService(userAdapter, userAdapter);
        registerController = new RegisterController(registerService);
    }
}