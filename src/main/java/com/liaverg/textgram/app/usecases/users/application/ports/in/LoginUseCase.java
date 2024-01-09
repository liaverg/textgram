package com.liaverg.textgram.app.usecases.users.application.ports.in;

import com.liaverg.textgram.app.usecases.users.domain.commands.LoginCommand;

public interface LoginUseCase {
    long login(LoginCommand command);
}
