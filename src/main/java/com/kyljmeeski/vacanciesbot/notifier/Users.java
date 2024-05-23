package com.kyljmeeski.vacanciesbot.notifier;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class Users {

    private final Connection connection;

    public Users(Connection connection) {
        this.connection = connection;
    }

    public List<User> all() {
        List<User> users = new ArrayList<>();
        String sql = "SELECT * from users";
        try {
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(sql);
            while (resultSet.next()) {
                User user = new User(resultSet.getString("chat_id"));
                users.add(user);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return users;
    }

}
