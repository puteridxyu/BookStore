package com.bookstore.app;

import java.sql.Connection;
import java.sql.DriverManager;

public class TestDBConnection {

    public static void main(String[] args) {
        String url = "jdbc:postgresql://localhost:5432/bookstore";
        String username = "postgres";
        String password = "eAT6&kILL20";// replace this

        try (Connection connection = DriverManager.getConnection(url, username, password)) {
            if (connection != null) {
                System.out.println("Connected to the database successfully!");
            } else {
                System.out.println("Failed to make connection!");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
