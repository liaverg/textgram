package com.liaverg.textgram.app.usecases.users.application.ports.out;

public interface SaveUserPort {
    boolean saveUser(String username, String password, String role);
}
