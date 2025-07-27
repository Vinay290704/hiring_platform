package com.example.Connection;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConnectionManager {
    private static Connection connection;
    private static String dbUrl;
    private static String user;
    private static String password;

    public static void initialize(String dbUrl, String user, String password) {
        ConnectionManager.dbUrl = dbUrl;
        ConnectionManager.user = user;
        ConnectionManager.password = password;
    }

    public static Connection getConnection() throws SQLException {
        if (connection == null || connection.isClosed()) {
            if (dbUrl == null) {
                throw new IllegalStateException("ConnectionManager not initialized. Call initialize() first.");
            }
            connection = DriverManager.getConnection(dbUrl, user, password);
        }
        return connection;
    }

    public static void closeConnection() {
        if (connection != null) {
            try {
                connection.close();
                System.out.println("Database connection closed.");
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
}
