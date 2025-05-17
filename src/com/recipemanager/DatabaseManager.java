package com.recipemanager;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseManager {
    // Updated to match your exact configuration
    private static final String URL = "jdbc:mysql://localhost:3306/recipe_manager";
    private static final String USER = "recipe_app_user";
    private static final String PASSWORD = "strongpassword123";

    private static Connection connection;

    public static Connection getConnection() throws SQLException {
        if (connection == null || connection.isClosed()) {
            connection = DriverManager.getConnection(URL, USER, PASSWORD);
        }
        return connection;
    }

    // Test connection method (run this first!)
    public static void main(String[] args) {
        try {
            Connection conn = getConnection();
            System.out.println("✅ Connected to MySQL successfully!");
            
            // Verify tables are accessible
            System.out.println("Testing tables...");
            conn.createStatement().executeQuery("SELECT 1 FROM recipes LIMIT 1");
            conn.createStatement().executeQuery("SELECT 1 FROM categories LIMIT 1");
            conn.createStatement().executeQuery("SELECT 1 FROM difficulty_levels LIMIT 1");
            System.out.println("✅ All required tables are accessible");
            
            conn.close();
        } catch (SQLException e) {
            System.out.println("❌ Con00nection failed: " + e.getMessage());
            e.printStackTrace();
        }
    }
}