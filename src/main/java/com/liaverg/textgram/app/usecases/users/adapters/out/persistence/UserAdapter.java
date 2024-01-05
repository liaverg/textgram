package com.liaverg.textgram.app.usecases.users.adapters.out.persistence;

import com.liaverg.textgram.app.usecases.users.application.ports.out.LoadUserPort;
import com.liaverg.textgram.app.usecases.users.application.ports.out.SaveUserPort;
import com.liaverg.textgram.app.usecases.users.domain.User;
import com.liaverg.textgram.app.utilities.DbUtils;
import com.liaverg.textgram.app.utilities.DbUtils.ConnectionConsumer;
import com.liaverg.textgram.app.utilities.DbUtils.ConnectionFunction;

import java.sql.PreparedStatement;

public class UserAdapter implements SaveUserPort, LoadUserPort {
    @Override
    public User loadUserByUsername(String username) {
        // Connection Function
        return null;
    }

    @Override
    public void saveUser(String username, String password, String role) {
        ConnectionConsumer saveUserInDb = connection -> {
            String insertSQL = "INSERT INTO textgram.users (username, password, role) VALUES (?, ?, ?)";
            try (PreparedStatement insertStatement = connection.prepareStatement(insertSQL)) {
                insertStatement.setString(1, username);
                insertStatement.setString(2, password);
                insertStatement.setString(3, role);
                insertStatement.executeUpdate();
            }
        };
        try{
            DbUtils.executeStatementsInTransaction(saveUserInDb);
        } catch (Exception e) {
            throw new RuntimeException("Failed to Save User");
        }
    }
}
