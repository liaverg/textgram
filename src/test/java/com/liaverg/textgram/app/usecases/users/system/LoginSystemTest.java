package com.liaverg.textgram.app.usecases.users.system;

import com.liaverg.textgram.app.usecases.users.adapters.in.web.LoginController;
import com.liaverg.textgram.app.usecases.users.adapters.out.persistence.UserAdapter;
import com.liaverg.textgram.app.usecases.users.application.services.LoginService;
import com.liaverg.textgram.app.usecases.users.domain.commands.LoginCommand;
import com.liaverg.textgram.app.utilities.DbUtils;
import com.liaverg.textgram.appconfig.DataSourceProvider;
import com.zaxxer.hikari.HikariDataSource;
import io.javalin.Javalin;
import io.javalin.http.HttpStatus;
import io.javalin.json.JavalinJackson;
import io.javalin.testtools.JavalinTest;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

@Testcontainers
public class LoginSystemTest {
    @Container
    private static final PostgreSQLContainer<?> postgres =
            new PostgreSQLContainer<>("postgres:16")
                    .withInitScript("init.sql");
    private static HikariDataSource dataSource;
    private static final int LEAK_DETECTION_THRESHOLD = 3000;
    private static Javalin app;
    private static final JavalinJackson javalinJackson = new JavalinJackson();


    @BeforeEach
    void setup(){
        UserAdapter userAdapter = new UserAdapter();
        DataSourceProvider dataSourceProvider = new DataSourceProvider(
                postgres.getJdbcUrl(),
                postgres.getUsername(),
                postgres.getPassword(),
                LEAK_DETECTION_THRESHOLD
        );
        new DbUtils(dataSourceProvider.getHikariProxyDataSource());
        dataSource = dataSourceProvider.getHikariDataSource();

        LoginService loginService = new LoginService(userAdapter);
        LoginController loginController = new LoginController(loginService);

        app = Javalin.create();
        app.post("/users/login", loginController::login);
    }

    @AfterEach
    void tearDown() throws Exception {
        truncateTable();
    }

    private void truncateTable() throws SQLException {
        try (Connection connection = dataSource.getConnection()) {
            String truncateSQL = "TRUNCATE TABLE textgram.users";
            try (PreparedStatement truncateStatement = connection.prepareStatement(truncateSQL)) {
                truncateStatement.executeUpdate();
            }
        }
    }

    private void insertUser(String username, String password, String role){
        try(Connection connection = dataSource.getConnection()){
            String insertSQL = "INSERT INTO textgram.users (username, password, role) VALUES (?, ?, ?)";
            try (PreparedStatement insertStatement = connection.prepareStatement(insertSQL)) {
                insertStatement.setString(1, username);
                insertStatement.setString(2, password);
                insertStatement.setString(3, role);
                insertStatement.executeUpdate();
            }
        } catch (SQLException e){
            fail();
        }
    }

    private static String toJson(LoginCommand command) {
        return javalinJackson.toJsonString(command, LoginCommand.class);
    }

    @Test
    @DisplayName("Successful Login")
    public void should_login_user_when_happy_day_scenario(){
        insertUser("username@gmail.com", "User1234!", "FREE");
        LoginCommand command = new LoginCommand("username@gmail.com", "User1234!");

        JavalinTest.test(app, (server, client) -> {
            try(var response = client.post("/users/login", toJson(command))){
                assertEquals(HttpStatus.OK.getCode(), response.code());
                assertEquals("text/plain", response.header("content-type"));
                assertEquals("User 1 Logged In Successfully", response.body().string());
            }
        });
    }

    @Test
    @DisplayName("Failed Login when Username Does not Exist")
    public void should_fail_to_login_user_when_username_does_not_exist(){
        LoginCommand command = new LoginCommand("username@gmail.com", "User1234!");

        JavalinTest.test(app, (server, client) -> {
            try( var response = client.post("/users/login", toJson(command))){
                assertEquals(HttpStatus.BAD_REQUEST.getCode(), response.code());
                assertEquals("text/plain", response.header("content-type"));
                assertEquals("Username Does not Exist", response.body().string());
            }
        });
    }

    @Test
    @DisplayName("Failed Login when Password is Wrong")
    public void should_fail_to_login_user_when_password_is_wrong(){
        insertUser("username@gmail.com", "User1234!", "FREE");
        LoginCommand command = new LoginCommand("username@gmail.com", "User1234!!!");

        JavalinTest.test(app, (server, client) -> {
            try( var response = client.post("/users/login", toJson(command))){
                assertEquals(HttpStatus.BAD_REQUEST.getCode(), response.code());
                assertEquals("text/plain", response.header("content-type"));
                assertEquals("Password is Wrong", response.body().string());
            }
        });
    }
}
