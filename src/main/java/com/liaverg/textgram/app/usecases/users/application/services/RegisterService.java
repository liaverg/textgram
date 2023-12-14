package com.liaverg.textgram.app.usecases.users.application.services;

import com.liaverg.textgram.app.usecases.users.application.port.out.LoadUserPort;
import com.liaverg.textgram.app.usecases.users.domain.commands.RegisterCommand;
import com.liaverg.textgram.app.usecases.users.application.port.in.RegisterUseCase;
import com.liaverg.textgram.app.usecases.users.application.port.out.SaveUserPort;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class RegisterService implements RegisterUseCase {
    private final SaveUserPort saveUserPort;
    private final LoadUserPort loadUserPort;

    @Override
    public boolean register(RegisterCommand command) {
        if (loadUserPort.loadUser(command.username()) != null){
            return false;
        }
        return saveUserPort.saveUser(command.username(), command.password(), command.role());
    }
}
