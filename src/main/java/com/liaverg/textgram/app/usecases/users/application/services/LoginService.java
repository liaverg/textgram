package com.liaverg.textgram.app.usecases.users.application.services;

import com.liaverg.textgram.app.usecases.users.domain.commands.LoginCommand;
import com.liaverg.textgram.app.usecases.users.application.ports.in.LoginUseCase;
import com.liaverg.textgram.app.usecases.users.application.ports.out.LoadUserPort;
import com.liaverg.textgram.app.usecases.users.domain.ids.UserId;
import com.liaverg.textgram.app.usecases.users.domain.views.UserDTO;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class LoginService implements LoginUseCase {
    private final LoadUserPort loadUserPort;

    @Override
    public UserId login(LoginCommand command) {
        UserDTO savedUser = loadUserPort.loadUserByUsername(command.username());
        if (savedUser == null){
            return null;
        }
        if(savedUser.getPassword().equals(command.password()))
            return new UserId(savedUser.getId());
        return new UserId(-1);
    }
}
