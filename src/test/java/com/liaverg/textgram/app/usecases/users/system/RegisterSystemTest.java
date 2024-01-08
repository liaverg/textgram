package com.liaverg.textgram.app.usecases.users.system;

import com.liaverg.textgram.app.usecases.users.adapters.in.web.RegisterController;
import com.liaverg.textgram.app.usecases.users.adapters.out.persistence.UserAdapter;
import com.liaverg.textgram.app.usecases.users.application.services.RegisterService;
import com.liaverg.textgram.app.usecases.users.domain.commands.RegisterCommand;
import com.liaverg.textgram.app.utilities.DbUtils;
import com.liaverg.textgram.appconfig.DataSourceProvider;
import com.zaxxer.hikari.HikariDataSource;
import io.javalin.Javalin;
import io.javalin.http.HttpStatus;
import io.javalin.json.JavalinJackson;
import io.javalin.testtools.JavalinTest;
import org.junit.jupiter.api.*;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

@Testcontainers
public class RegisterSystemTest {
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

        RegisterService registerService = new RegisterService(userAdapter, userAdapter);
        RegisterController registerController = new RegisterController(registerService);

        app = Javalin.create();
        app.post("/users/register", registerController::register);
    }

    @AfterEach
    void tearDown() throws Exception {
        addRoleColumn();
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

    private void addRoleColumn() throws SQLException {
        try (Connection connection = dataSource.getConnection()) {
            String addColumnSQL = "ALTER TABLE textgram.users ADD COLUMN IF NOT EXISTS role VARCHAR(50)";
            try (PreparedStatement addColumnStatement = connection.prepareStatement(addColumnSQL)) {
                addColumnStatement.executeUpdate();
            }
        }
    }

    private void dropRoleColumn(){
        try (Connection connection = dataSource.getConnection()) {
            String dropColumnSQL = "ALTER TABLE textgram.users DROP COLUMN role";
            try (PreparedStatement dropColumnStatement = connection.prepareStatement(dropColumnSQL)) {
                dropColumnStatement.executeUpdate();
            }
        } catch (SQLException e){
            fail();
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

    private static String toJson(RegisterCommand command) {
        return javalinJackson.toJsonString(command, RegisterCommand.class);
    }

    @Test
    @DisplayName("Successful Register")
    public void should_register_user_when_happy_day_scenario(){
        RegisterCommand command = new RegisterCommand("username@gmail.com", "User1234!", "FREE");

        JavalinTest.test(app, (server, client) -> {
            try(var response = client.post("/users/register", toJson(command))){
                assertEquals(HttpStatus.CREATED.getCode(), response.code());
                assertEquals("text/plain", response.header("content-type"));
                assertEquals("User Registered Successfully", response.body().string());
            }
        });
    }

    @Test
    @DisplayName("Failed Register")
    public void should_fail_to_register_user_when_user_already_exists(){
        insertUser("username@gmail.com", "User1234!", "FREE");
        RegisterCommand command = new RegisterCommand("username@gmail.com", "User1234!", "FREE");

        JavalinTest.test(app, (server, client) -> {
            try( var response = client.post("/users/register", toJson(command))){
                assertEquals(HttpStatus.BAD_REQUEST.getCode(), response.code());
                assertEquals("text/plain", response.header("content-type"));
                assertEquals("User Already Exists", response.body().string());
            }
        });
    }

    @Test
    @DisplayName("Failed Register when Users Table Configuration Is Incorrect")
    public void should_fail_to_save_user_when_users_table_has_no_role_column(){
        dropRoleColumn();
        RegisterCommand command = new RegisterCommand("username@gmail.com", "User1234!", "FREE");

        JavalinTest.test(app, (server, client) -> {
            try( var response = client.post("/users/register", toJson(command))){
                assertEquals(HttpStatus.BAD_REQUEST.getCode(), response.code());
                assertEquals("text/plain", response.header("content-type"));
                assertEquals("Failed to Load User", response.body().string());
            }
        });
    }
}
