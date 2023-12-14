package com.liaverg.textgram.app.usecases.users.application.port.out;

import com.liaverg.textgram.app.usecases.users.domain.User;

public interface LoadUserPort {
    User loadUser(String username);
}
