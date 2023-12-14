package com.liaverg.textgram.app.usecases.users.adapters.in.web;

import io.javalin.Javalin;
import io.javalin.testtools.JavalinTest;
import org.junit.jupiter.api.Test;

class RegisterControllerTest {

    @Test
    public void should_register_user_when_happy_day_scenario(){
        String[] postedBody = {"username@gmail.com", "User1234!", "free"};
        Javalin app = Javalin.create().post("/textgram/register", ctx -> {
            postedBody[0] = ctx.body();
        }).start(7070);

        JavalinTest.test(app, (server, client) -> {
            //assert stuff
        });
    }

}