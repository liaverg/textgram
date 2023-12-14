package com.liaverg.textgram.app.usecases.users.application.port.in;

import com.liaverg.textgram.app.usecases.users.domain.commands.RegisterCommand;

public interface RegisterUseCase {
    boolean register(RegisterCommand command);
}
