package com.developer.database;

import com.developer.models.User;

import java.io.File;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class Database {
    private Connection connection;
    private Statement statement;

    public Database() {
        try {
            if (!new File("database").exists()) {
                new File("database").mkdir();
            }
            connection = DriverManager.getConnection("jdbc:sqlite:database/auth-plugin.db");
            statement = connection.createStatement();
            String query = "CREATE TABLE IF NOT EXISTS USER (" +
                    "ID INTEGER PRIMARY KEY AUTOINCREMENT," +
                    "NAME TEXT NOT NULL," +
                    "EMAIL TEXT NOT NULL," +
                    "PASSWORD TEXT NOT NULL" +
                    ");";
            statement.execute(query);
        } catch (SQLException throwable) {
            throwable.printStackTrace();
        }
    }

    public User getUserByName(String name) {
        List<User> users = new ArrayList<>();
        try {
            statement = connection.createStatement();
            String query = "SELECT * FROM USER WHERE NAME = " + String.format("'%s';", name);
            ResultSet resultSet = statement.executeQuery(query);
            while (resultSet.next()) {
                User user = new User();
                user.setId(resultSet.getInt("ID"));
                user.setName(resultSet.getString("NAME"));
                user.setEmail(resultSet.getString("EMAIL"));
                user.setPassword(resultSet.getString("PASSWORD"));
                users.add(user);
            }
            if (users.size() > 0)
                return users.get(0);
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return null;
    }

    public boolean userExistByName(String name) {
        try {
            statement = connection.createStatement();
            String query = "SELECT * FROM USER WHERE NAME = \'" + name + "\'";
            ResultSet resultSet = statement.executeQuery(query);
            while (resultSet.next()) {
                return true;
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return false;
    }

    public void save(User user) {
        try {
            statement = connection.createStatement();
            String query = "INSERT INTO USER (NAME, EMAIL, PASSWORD)"
                    + " VALUES "
                    + String.format("('%s', '%s', '%s')", user.getName(), user.getEmail(), user.getPassword());
            statement.executeUpdate(query);
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }
}
