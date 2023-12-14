package com.liaverg.textgram.app.usecases.users.application.port.out;

public interface SaveUserPort {
    boolean saveUser(String username, String password, String role);
}
