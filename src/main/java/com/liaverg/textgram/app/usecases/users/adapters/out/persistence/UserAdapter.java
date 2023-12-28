package com.liaverg.textgram.app.usecases.users.adapters.out.persistence;

import com.liaverg.textgram.app.usecases.users.application.ports.out.LoadUserPort;
import com.liaverg.textgram.app.usecases.users.application.ports.out.SaveUserPort;
import com.liaverg.textgram.app.usecases.users.domain.User;

public class UserAdapter implements SaveUserPort, LoadUserPort {
    @Override
    public User loadUserByUsername(String username) {
        return null;
    }

    @Override
    public void saveUser(String username, String password, String role) {

    }
}
