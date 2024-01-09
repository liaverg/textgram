package com.liaverg.textgram.app.usecases.users.application.ports.in;

import com.liaverg.textgram.app.usecases.users.domain.commands.LoginCommand;
import com.liaverg.textgram.app.usecases.users.domain.ids.UserId;

public interface LoginUseCase {
    UserId login(LoginCommand command);
}
