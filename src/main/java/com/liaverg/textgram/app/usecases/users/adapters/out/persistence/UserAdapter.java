package com.liaverg.textgram.app.usecases.users.adapters.out.persistence;

import com.liaverg.textgram.app.usecases.users.application.ports.out.LoadUserPort;
import com.liaverg.textgram.app.usecases.users.application.ports.out.SaveUserPort;
import com.liaverg.textgram.app.usecases.users.domain.views.UserDTO;
import com.liaverg.textgram.app.utilities.DbUtils;
import com.liaverg.textgram.app.utilities.DbUtils.ConnectionConsumer;
import com.liaverg.textgram.app.utilities.DbUtils.ConnectionFunction;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class UserAdapter implements SaveUserPort, LoadUserPort {
    @Override
    public UserDTO loadUserByUsername(String username) {
        ConnectionFunction loadUserByUsernameFromDb = connection -> {
            String selectSQL = "SELECT user_id, username, password, role FROM textgram.users";
            try (PreparedStatement selectStatement = connection.prepareStatement(selectSQL)) {
                try (ResultSet resultSet = selectStatement.executeQuery()) {
                    List<String> userList = new ArrayList<>();
                    while (resultSet.next()) {
                        userList.add(resultSet.getString("user_id"));
                        userList.add(resultSet.getString("username"));
                        userList.add(resultSet.getString("password"));
                        userList.add(resultSet.getString("role"));
                    }
                    if(userList.isEmpty())
                        return null;
                    return new UserDTO(Long.parseLong(userList.get(0)),
                            userList.get(1), userList.get(2), userList.get(3));
                }
            }
        };
        try {
            return (UserDTO) DbUtils.executeStatementsInTransactionWithResult(loadUserByUsernameFromDb);
        } catch (Exception e){
            throw new RuntimeException("Failed to Load User");
        }
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
        try {
            DbUtils.executeStatementsInTransaction(saveUserInDb);
        } catch (Exception e) {
            throw new RuntimeException("Failed to Save User");
        }
    }
}
