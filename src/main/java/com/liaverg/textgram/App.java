package com.liaverg.textgram;

import com.liaverg.textgram.app.usecases.users.adapters.in.web.RegisterController;
import com.liaverg.textgram.appconfig.AppConfig;
import io.javalin.Javalin;
import io.javalin.openapi.plugin.OpenApiPlugin;
import io.javalin.openapi.plugin.OpenApiPluginConfiguration;
import io.javalin.openapi.plugin.swagger.SwaggerConfiguration;
import io.javalin.openapi.plugin.swagger.SwaggerPlugin;

import static io.javalin.apibuilder.ApiBuilder.*;

public class App {
    public static void main(String[] args) {
        AppConfig appConfig = new AppConfig();
        RegisterController registerController = appConfig.getRegisterController();

        Javalin.create(config -> {
            config.plugins.register(new OpenApiPlugin(new OpenApiPluginConfiguration()
                    .withDocumentationPath("openapi")
                    .withDefinitionConfiguration((version, definition) -> definition
                            .withOpenApiInfo((openApiInfo) -> {
                                openApiInfo.setTitle("Textgram");
                                openApiInfo.setVersion("1.0.0");
                                openApiInfo.setDescription("A text-based social media API");
                            })
                    )
            ));
            config.plugins.register(new SwaggerPlugin(new SwaggerConfiguration()));
        }).routes(() -> {
            path("users/register", () -> {
                post(registerController::register);
            });
        }).start(8080);

        System.out.println("Check out Swagger UI docs at http://localhost:8080/swagger");
    }
}