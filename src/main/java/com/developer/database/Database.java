package com.developer.database;

import com.developer.models.Detail;
import com.developer.models.User;

import java.io.File;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Database {

    private Connection connection;
    private Statement statement;

    public Database() {
        try {
            File file = new File("database");
            if (!file.exists()) {
                file.mkdirs();
            }
            connection = DriverManager.getConnection("jdbc:sqlite:database/auth-plugin.db");
            statement = connection.createStatement();
            String query = "CREATE TABLE IF NOT EXISTS USER ("
                    + "ID INTEGER PRIMARY KEY AUTOINCREMENT,"
                    + "USERNAME TEXT NOT NULL,"
                    + "EMAIL TEXT NOT NULL,"
                    + "PASSWORD TEXT NOT NULL"
                    + ");";
            statement.execute(query);
            query = "CREATE TABLE IF NOT EXISTS DETAIL (" +
                    "USER_ID INTEGER NOT NULL," +
                    "WORLD_NAME TEXT NOT NULL," +
                    "P_X DOUBLE NOT NULL," +
                    "P_Y DOUBLE NOT NULL," +
                    "P_Z DOUBLE NOT NULL"
                    + ");";
            statement.execute(query);
            statement.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void save(User user) {
        try {
            statement = connection.createStatement();
            String query = "INSERT INTO USER (USERNAME, EMAIL, PASSWORD) VALUES " + String.format("('%s', '%s', '%s');", user.getUsername(), user.getEmail(), user.getPassword());
            statement.executeUpdate(query);
        } catch (SQLException ex) {
            Logger.getLogger(Database.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void save(Detail detail) {
        try {
            statement = connection.createStatement();
            String query = "INSERT INTO DETAIL (USER_ID, WORLD_NAME, P_X, P_Y, P_Z) VALUES " +
                    String.format("( %d, '%s', %f, %f, %f);", detail.getUserId(), detail.getWorldName(), detail.getX(), detail.getY(), detail.getZ());
            statement.executeUpdate(query);
        } catch (SQLException ex) {
            Logger.getLogger(Database.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void update(Detail detail) {
        try {
            statement = connection.createStatement();
            String query = "UPDATE DETAIL SET " +
                    String.format("WORLD_NAME='%s', P_X=%f, P_Y=%f, P_Z=%f WHERE USER_ID = %d;", detail.getWorldName(), detail.getX(), detail.getY(), detail.getZ(), detail.getUserId());
            statement.executeUpdate(query);
        } catch (SQLException ex) {
            Logger.getLogger(Database.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public User getUserByUsername(String username) {
        List<User> users = new ArrayList<>();
        try {
            statement = connection.createStatement();
            String query = String.format("SELECT * FROM USER WHERE USERNAME = '%s';", username);
            ResultSet resultSet = statement.executeQuery(query);
            while (resultSet.next()) {
                User user = new User();
                user.setId(resultSet.getInt("ID"));
                user.setUsername(resultSet.getString("USERNAME"));
                user.setEmail(resultSet.getString("EMAIL"));
                user.setPassword(resultSet.getString("PASSWORD"));
                users.add(user);
            }
            if (users.size() > 0) {
                return users.get(0);
            }
        } catch (SQLException ex) {
            Logger.getLogger(Database.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    public Detail getDetailByUserId(int id) {
        List<Detail> details = new ArrayList<>();
        try {
            statement = connection.createStatement();
            String query = String.format("SELECT * FROM DETAIL WHERE USER_ID = %d;", id);
            ResultSet resultSet = statement.executeQuery(query);
            while (resultSet.next()) {
                Detail detail = new Detail();
                detail.setUserId(resultSet.getInt("USER_ID"));
                detail.setWorldName(resultSet.getString("WORLD_NAME"));
                detail.setX(resultSet.getDouble("P_X"));
                detail.setY(resultSet.getDouble("P_Y"));
                detail.setZ(resultSet.getDouble("P_Z"));
                details.add(detail);
            }
            if (details.size() > 0) {
                return details.get(0);
            }
        } catch (SQLException ex) {
            Logger.getLogger(Database.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }


}
