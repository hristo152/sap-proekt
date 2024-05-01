package org.example;

import java.io.IOException;
import java.sql.*;
import java.util.Scanner;

public class Main {
    private static Connection connection;
    private static Scanner scanner;

    private static void connectToDatabase() {
        try {
            connection = DriverManager.getConnection(
                    "jdbc:mysql://localhost:3306/onlinevintageshop", "root", "");
            if (connection != null) {
                System.out.println("Connected to the database!");
            }
        } catch (SQLException e) {
            System.out.println("Failed to connect to the database: " + e.getMessage());
        }
    }

    private static User logInUser() {
        scanner = new Scanner(System.in);
        System.out.println("Enter username:");
        String username = scanner.nextLine();
        System.out.println("Enter password:");
        String password = scanner.nextLine();

        String sql = "SELECT * FROM users WHERE username = ? AND password = ?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, username);
            statement.setString(2, password);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                System.out.println("Login successful!");

                UserRole role = UserRole.valueOf(resultSet.getString("role").toUpperCase());
                return new User(
                        resultSet.getInt("id"),
                        resultSet.getString("username"),
                        role,
                        resultSet.getString("email"),
                        resultSet.getString("address"));
            } else {
                System.out.println("Incorrect username or password. Try again.");
                logInUser();
                return null;
            }
        } catch (SQLException e) {
            System.out.println("Error logging in: " + e.getMessage());
            return null;
        }
    }

    private static void displayUserMenu() {
        System.out.println("Please choose from the menu:");
        System.out.println("1. View All Products");
        System.out.println("2. View All Products With Discount");
        System.out.println("3. View Shopping cart");
        System.out.println("4. Exit");
    }

    private static void displayAdminMenu() {
        System.out.println("Please choose from the menu:");
        System.out.println("1. View All Products");
        System.out.println("2. View All Products With Discount");
        System.out.println("3. Add New Product");
        System.out.println("4. Edit Product");
        System.out.println("5. Delete Product");
        System.out.println("6. Exit");
    }

    private static void displayAllProducts() {
        try {
            String sql = "SELECT * FROM products";
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(sql);

            System.out.println("All Products:");
            while (resultSet.next()) {
                double price = resultSet.getDouble("price");
                Integer discount = resultSet.getInt("discount"); // Use Integer to handle null values
                double discountedPrice;

                System.out.println("Product ID: " + resultSet.getInt("id"));
                System.out.println("Name: " + resultSet.getString("name"));
                if (discount > 0) {
                    discountedPrice = price - (price * discount / 100.0);
                    System.out.println("Price: " + discountedPrice);
                    System.out.println("Discount: " + discount + "%");
                } else {
                    discountedPrice = price;
                    System.out.println("Price: " + discountedPrice);
                    System.out.println("No discount available.");
                }
                System.out.println("Manufacturing Year: " + resultSet.getInt("manufacturing_year"));
                System.out.println("Quality: " + resultSet.getString("quality"));
                System.out.println("Quantity: " + resultSet.getInt("quantity"));
                System.out.println("Description: " + resultSet.getString("description"));
                System.out.println("----------------------");
            }
        } catch (SQLException e) {
            System.out.println("Error displaying products: " + e.getMessage());
        }
    }

    private static void displayAllProductsWithDiscount() {
        try {
            String sql = "SELECT p.id, p.name, p.price, p.manufacturing_year, p.quality, p.quantity, p.description, p.discount " +
                    "FROM products p " +
                    "WHERE p.discount IS NOT NULL";
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(sql);

            System.out.println("Products with Discount:");
            while (resultSet.next()) {
                double price = resultSet.getDouble("price");
                Integer discount = resultSet.getInt("discount"); // Use Integer to handle null values
                double discountedPrice = price - (price * discount / 100.0);

                System.out.println("Product ID: " + resultSet.getInt("id"));
                System.out.println("Name: " + resultSet.getString("name"));
                System.out.println("Price: " + discountedPrice);System.out.println("Discount: " + discount + "%");
                System.out.println("Manufacturing Year: " + resultSet.getInt("manufacturing_year"));
                System.out.println("Quality: " + resultSet.getString("quality"));
                System.out.println("Quantity: " + resultSet.getInt("quantity"));
                System.out.println("Description: " + resultSet.getString("description"));
                System.out.println("----------------------");
            }
        } catch (SQLException e) {
            System.out.println("Error displaying products with discount: " + e.getMessage());
        }
    }


    public static void main(String[] args) throws IOException {
        int choice;
        connectToDatabase();

        User loggedInUser = logInUser();
        if (loggedInUser != null) {
            if (loggedInUser.getRole() == UserRole.USER) {
                displayUserMenu();
                choice = scanner.nextInt();

                switch (choice){
                    case 1:
                        displayAllProducts();
                        break;
                    case 2:
                        displayAllProductsWithDiscount();
                        break;
                }


            } else if (loggedInUser.getRole() == UserRole.ADMIN) {
                displayAdminMenu();
                choice = scanner.nextInt();

                switch (choice){
                    case 1:
                        displayAllProducts();
                        break;
                    case 2:
                        displayAllProductsWithDiscount();
                        break;
                }
            }
        } else {
            System.out.println("Login failed. Exiting program.");
        }
    }
}
