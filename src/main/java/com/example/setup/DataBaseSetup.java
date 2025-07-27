package com.example.setup;

import com.example.Connection.ConnectionManager;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;
import java.util.Scanner;
import java.util.stream.Collectors;

public class DataBaseSetup {
    public static void setupDatabase(Scanner scanner) {
        try {
            System.out.println("--- Automatic Database Setup ---");
            System.out.println("Please provide your MySQL credentials to set up the database.");
            System.out.print("Enter MySQL Username (e.g., root): ");
            String user = scanner.nextLine();
            System.out.print("Enter MySQL Password: ");
            String password = scanner.nextLine();

            String host = "localhost";
            String port = "3306";
            String dbName = "recruitment_db";

            String serverUrl = "jdbc:mysql://" + host + ":" + port + "?allowPublicKeyRetrieval=true&useSSL=false&allowMultiQueries=true";
            try (Connection conn = DriverManager.getConnection(serverUrl, user, password);
                 Statement stmt = conn.createStatement()) {
                System.out.println("Checking for database '" + dbName + "'...");
                stmt.executeUpdate("CREATE DATABASE IF NOT EXISTS " + dbName);
                System.out.println("Database is ready.");
            }

            String dbUrl = "jdbc:mysql://" + host + ":" + port + "/" + dbName + "?allowPublicKeyRetrieval=true&useSSL=false";
            try (Connection conn = DriverManager.getConnection(dbUrl, user, password)) {
                System.out.println("Connected to '" + dbName + "'. Executing SQL scripts...");
                executeSqlScript(conn, "schema.sql");
                executeSqlScript(conn, "Sql_Raw.sql");
            }

            ConnectionManager.initialize(dbUrl, user, password);

            System.out.println("--- Database setup completed successfully! ---");

        } catch (Exception e) {
            System.err.println("\nFATAL: Database setup failed. The application cannot start.");
            e.printStackTrace();
            System.exit(1);
        }
    }

    private static void executeSqlScript(Connection conn, String fileName) throws Exception {
        System.out.println("Executing script: " + fileName);
        String sqlScript;
        try (InputStream inputStream = DataBaseSetup.class.getClassLoader().getResourceAsStream(fileName)) {
            if (inputStream == null) {
                throw new Exception("Cannot find '" + fileName + "' in resources.");
            }
            try (BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream))) {
                sqlScript = reader.lines().collect(Collectors.joining("\n"));
            }
        }
        String[] statements = sqlScript.split(";(?=(?:[^\"]*\"[^\"]*\")*[^\"]*$)");
        try (Statement stmt = conn.createStatement()) {
            for (String statement : statements) {
                if (statement.trim().isEmpty() || statement.trim().startsWith("--")) {
                    continue;
                }
                stmt.execute(statement);
            }
        }
        System.out.println("Successfully executed script: " + fileName);
    }
}
