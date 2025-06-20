package com.bookstore.app;

import java.sql.Connection;
import java.sql.DriverManager;

public class TestDBConnection {
    public static void main(String[] args) {
        String jdbcUrl = "jdbc:postgresql://localhost:5432/bookstore";
        String username = "postgres";
        String password = "eAT6&kILL20";// replace this

        try {
            Connection conn = DriverManager.getConnection(jdbcUrl, username, password);
            System.out.println("  Connected to database successfully!");
            conn.close();
        } catch (Exception e) {
            System.err.println("  Failed to connect:");
            e.printStackTrace();
        }
    }
}