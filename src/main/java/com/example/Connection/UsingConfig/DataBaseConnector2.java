package com.example.Connection.UsingConfig;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public class DataBaseConnector2 {
    private static final Properties properties = new Properties();
    static {
        try (InputStream input = DataBaseConnector2.class.getClassLoader().getResourceAsStream("config.properties")) {
            if (input == null) {
                System.out.println("Sorry, unable to find config.properties");
            } else {
                properties.load(input);
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    public static Connection getConnection() throws SQLException {
        String url = String.format("jdbc:mysql://%s:%s/%s",
                properties.getProperty("DB_HOST"),
                properties.getProperty("DB_PORT"),
                properties.getProperty("DATABASE_NAME"));

        return DriverManager.getConnection(
                url,
                properties.getProperty("DB_USER"),
                properties.getProperty("DB_PASSWORD"));
    }
    public static Properties getProperties() {
        return properties;
    }
}
