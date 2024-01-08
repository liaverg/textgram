package com.liaverg.textgram.app.usecases.users.adapters.out.persistence;

import com.liaverg.textgram.app.usecases.users.domain.views.UserDTO;
import com.liaverg.textgram.app.utilities.DbUtils;
import com.liaverg.textgram.appconfig.DataSourceProvider;
import com.zaxxer.hikari.HikariDataSource;
import org.junit.jupiter.api.*;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.*;


@Testcontainers
class UserAdapterTest {
    private static UserAdapter userAdapter;

    @Container
    private static final PostgreSQLContainer<?> postgres =
            new PostgreSQLContainer<>("postgres:16")
                    .withInitScript("init.sql");
    private static HikariDataSource dataSource;
    private static final int LEAK_DETECTION_THRESHOLD = 3000;

    @BeforeAll
    static void setUp() {
        userAdapter = new UserAdapter();
        DataSourceProvider dataSourceProvider = new DataSourceProvider(
                postgres.getJdbcUrl(),
                postgres.getUsername(),
                postgres.getPassword(),
                LEAK_DETECTION_THRESHOLD
        );
        new DbUtils(dataSourceProvider.getHikariProxyDataSource());
        dataSource = dataSourceProvider.getHikariDataSource();
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

    private void verifyUserRecordInDatabase(String expectedUsername,
                                             String expectedPassword,
                                             String expectedRole){
        try (Connection connection = dataSource.getConnection()) {
            String selectSQL = "SELECT username, password, role FROM textgram.users";
            try (PreparedStatement selectStatement = connection.prepareStatement(selectSQL)) {
                try (ResultSet resultSet = selectStatement.executeQuery()) {
                    assertTrue(resultSet.next(), "Expected user record in the database");
                    assertEquals(expectedUsername, resultSet.getString("username"));
                    assertEquals(expectedPassword, resultSet.getString("password"));
                    assertEquals(expectedRole, resultSet.getString("role"));
                    assertFalse(resultSet.next());
                }
            }
        } catch (SQLException e){
            fail();
        }
    }

    private void verifyNoRecordInDatabase(){
        try (Connection connection = dataSource.getConnection()) {
            String selectSQL = "SELECT username FROM textgram.users";
            try (PreparedStatement selectStatement = connection.prepareStatement(selectSQL)) {
                try (ResultSet resultSet = selectStatement.executeQuery()) {
                    assertFalse(resultSet.next());
                }
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

    @Test
    @DisplayName("Successful Save")
    void should_save_user_when_happy_day_scenario() {
        String username = "username@gmail.com";
        String password = "User1234!";
        String role = "FREE";

        assertDoesNotThrow(() -> userAdapter.saveUser(username, password, role));

        verifyUserRecordInDatabase(username, password, role);
    }

    @Test
    @DisplayName("Failed Save when Users Table Configuration Is Incorrect")
    void should_fail_to_save_user_when_users_table_has_no_role_column() {
        dropRoleColumn();
        String username = "username@gmail.com";
        String password = "User1234!";
        String role = "FREE";

        RuntimeException exception =
                assertThrows(RuntimeException.class, () -> userAdapter.saveUser(username, password, role));

        assertEquals("Failed to Save User", exception.getMessage());
        verifyNoRecordInDatabase();
    }

    @Test
    @DisplayName("Successful Load when User Exists")
    void should_load_user_when_user_exists() {
        insertUser("username@gmail.com", "User1234!", "FREE");

        UserDTO user = userAdapter.loadUserByUsername("username@gmail.com");

        assertEquals("username@gmail.com", user.getUsername());
        assertEquals("User1234!", user.getPassword());
        assertEquals("FREE", user.getRole());
    }

    @Test
    @DisplayName("Failed Load when User Does Not Exist")
    void should_fail_to_load_user_when_user_does_not_exist() {
        UserDTO user = userAdapter.loadUserByUsername("username@gmail.com");

        assertNull(user);
    }

    @Test
    @DisplayName("Failed Load when Users Table Configuration Is Incorrect")
    void should_fail_to_load_user_when_users_table_has_no_role_column() {
        insertUser("username@gmail.com", "User1234!", "FREE");
        dropRoleColumn();

        RuntimeException exception =
                assertThrows(RuntimeException.class, () -> userAdapter.loadUserByUsername("username@gmail.com"));

        assertEquals("Failed to Load User", exception.getMessage());
    }
}