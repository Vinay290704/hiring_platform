package com.example.Connection.UsingDotenv;

import io.github.cdimascio.dotenv.Dotenv;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DataBaseConnector1 {
    private static final Dotenv dotenv = Dotenv.load();
    private static final String URL = "jdbc:mysql://" + dotenv.get("DB_HOST") + ":" + dotenv.get("DB_PORT") + "/" + dotenv.get("DATABASE_NAME");
    private static final String USER = dotenv.get("DB_USER");
    private static final String PASSWORD = dotenv.get("DB_PASSWORD");

    public static Connection getConnection() throws SQLException {
        System.out.println("URL: " + URL);
        System.out.println("USER: " + USER);
        System.out.println("PASSWORD: " + PASSWORD);
        return DriverManager.getConnection(URL , USER , PASSWORD);
    }
}
