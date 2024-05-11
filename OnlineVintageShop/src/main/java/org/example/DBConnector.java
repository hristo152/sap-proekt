package org.example;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public final class DBConnector {
    private static Connection connection;

    private DBConnector(Connection connection){
        this.connection = connection;
    }
    public static Connection connectToDatabase() {
        try {
            connection = DriverManager.getConnection(
                    "jdbc:mysql://localhost:3306/onlinevintageshop", "root", "");
            if (connection != null) {
                System.out.println("Connected to the database!");
                return connection;
            }
        } catch (SQLException e) {
            System.out.println("Failed to connect to the database: " + e.getMessage());
        }
        return null;
    }
}
