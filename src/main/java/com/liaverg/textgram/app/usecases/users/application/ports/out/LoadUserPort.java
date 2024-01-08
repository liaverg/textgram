package com.liaverg.textgram.app.usecases.users.application.ports.out;

import com.liaverg.textgram.app.usecases.users.domain.views.UserDTO;

public interface LoadUserPort {
    UserDTO loadUserByUsername(String username);
}
