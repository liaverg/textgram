package com.liaverg.textgram.app.usecases.users.application.ports.out;

public interface SaveUserPort {
    void saveUser(String username, String password, String role);
}
