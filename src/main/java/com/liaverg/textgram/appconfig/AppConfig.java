package com.liaverg.textgram.appconfig;

import com.liaverg.textgram.app.usecases.users.adapters.in.web.LoginController;
import com.liaverg.textgram.app.usecases.users.adapters.in.web.RegisterController;
import com.liaverg.textgram.app.usecases.users.adapters.out.persistence.UserAdapter;
import com.liaverg.textgram.app.usecases.users.application.services.LoginService;
import com.liaverg.textgram.app.usecases.users.application.services.RegisterService;
import com.liaverg.textgram.app.utilities.DbUtils;
import lombok.Getter;

@Getter
public class AppConfig {
    private final RegisterController registerController;
    private final LoginController loginController;

    public AppConfig() {
        UserAdapter userAdapter = new UserAdapter();
        RegisterService registerService = new RegisterService(userAdapter, userAdapter);
        registerController = new RegisterController(registerService);
        LoginService loginService = new LoginService(userAdapter);
        loginController = new LoginController(loginService);

        PropertiesReader propertiesReader = new PropertiesReader();
        DataSourceProvider dataSourceProvider = new DataSourceProvider(
                propertiesReader.getJdbcUrl(),
                propertiesReader.getUser(),
                propertiesReader.getPassword(),
                propertiesReader.getLeakDetectionThreshold());
        new DbUtils(dataSourceProvider.getHikariProxyDataSource());
    }
}