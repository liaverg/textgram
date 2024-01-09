package com.liaverg.textgram.app.usecases.users.application.services;

import com.liaverg.textgram.app.usecases.users.domain.commands.LoginCommand;
import com.liaverg.textgram.app.usecases.users.application.ports.in.LoginUseCase;
import com.liaverg.textgram.app.usecases.users.application.ports.out.LoadUserPort;
import com.liaverg.textgram.app.usecases.users.domain.views.UserDTO;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class LoginService implements LoginUseCase {
    private final LoadUserPort loadUserPort;
    //ToDo Better way for keeping the magic values without having to make them
    // fields in the classes that they are used
    private static final long NON_EXISTENT_USERNAME_CODE = -1;
    private static final long WRONG_PASSWORD_CODE = -2;

    @Override
    public long login(LoginCommand command) {
        UserDTO savedUser = loadUserPort.loadUserByUsername(command.username());
        if (savedUser == null){
            return NON_EXISTENT_USERNAME_CODE;
        }
        if(savedUser.getPassword().equals(command.password()))
            return savedUser.getId();
        return WRONG_PASSWORD_CODE;
    }
}
