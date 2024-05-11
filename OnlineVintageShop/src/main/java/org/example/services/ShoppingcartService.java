package org.example.services;

import java.sql.*;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class ShoppingcartService {
    private Connection connection;
    private Scanner scanner;

    public ShoppingcartService(Connection connection){
        this.connection = connection;
    }
    public void addToCart(int userId) {
        try {
            scanner = new Scanner(System.in);
            System.out.println("Enter product ID to add to cart:");
            int productId = scanner.nextInt();
            System.out.println("Enter quantity:");
            int quantity = scanner.nextInt();

            String sql = "INSERT INTO shopping_cart_items (product_id, quantity, shopping_cart_id) VALUES (?, ?, ?)";
            try (PreparedStatement statement = connection.prepareStatement(sql)) {
                statement.setInt(1, productId);
                statement.setInt(2, quantity);
                statement.setInt(3, userId);
                statement.executeUpdate();
            }
            System.out.println("You successfully added '" + getProductName(productId) + "' to your cart!");

            updateTotalSum(userId);
        } catch (SQLException e) {
            System.out.println("There is no such product id!");
        }
    }

    private String getProductName(int productId) throws SQLException {
        String sql = "SELECT name FROM products WHERE id = ?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, productId);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    return resultSet.getString("name");
                } else {
                    throw new SQLException("Product not found");
                }
            }
        }
    }

    private void updateTotalSum(int userId) {
        try {
            String sql = "SELECT SUM(products.price * shopping_cart_items.quantity) AS total_sum " +
                    "FROM shopping_cart_items " +
                    "JOIN products ON shopping_cart_items.product_id = products.id " +
                    "JOIN users ON shopping_cart_items.shopping_cart_id = users.id " +
                    "WHERE users.id = ?";

            try (PreparedStatement statement = connection.prepareStatement(sql)) {
                statement.setInt(1, userId);
                try (ResultSet resultSet = statement.executeQuery()) {
                    if (resultSet.next()) {
                        double totalSum = resultSet.getDouble("total_sum");

                        String updateSql = "UPDATE shopping_carts SET total_sum = ? WHERE user_id = ?";
                        try (PreparedStatement updateStatement = connection.prepareStatement(updateSql)) {
                            updateStatement.setDouble(1, totalSum);
                            updateStatement.setInt(2, userId);
                            updateStatement.executeUpdate();
                        }
                    }
                }
            }
        } catch (SQLException e) {
            System.out.println("Error updating total sum: " + e.getMessage());
        }
    }

    public void viewCart(int userId) {
        try {
            String sql = "SELECT p.name, p.price, sci.quantity " +
                    "FROM shopping_cart_items sci " +
                    "JOIN products p ON sci.product_id = p.id " +
                    "WHERE sci.shopping_cart_id = ?";

            Map<String, Integer> productQuantities = new HashMap<>();
            double totalSum = 0;

            try (PreparedStatement statement = connection.prepareStatement(sql)) {
                statement.setInt(1, userId);
                try (ResultSet resultSet = statement.executeQuery()) {
                    if (!resultSet.isBeforeFirst()) {
                        System.out.println("Your shopping cart is empty.");
                        return;
                    }

                    System.out.println("Shopping Cart:");
                    while (resultSet.next()) {
                        String productName = resultSet.getString("name");
                        int quantity = resultSet.getInt("quantity");
                        double price = resultSet.getDouble("price");

                        totalSum += price * quantity;

                        if (productQuantities.containsKey(productName)) {
                            quantity += productQuantities.get(productName);
                        }
                        productQuantities.put(productName, quantity);
                    }
                }
            }

            for (Map.Entry<String, Integer> entry : productQuantities.entrySet()) {
                String productName = entry.getKey();
                int quantity = entry.getValue();
                double price = getProductPrice(productName);

                System.out.println("Product Name: " + productName + ", Quantity: " + quantity + ", Price: $" + price);
            }

            System.out.println("Total Sum: $" + totalSum);
        } catch (SQLException e) {
            System.out.println("Error viewing cart: " + e.getMessage());
        }
    }

    private double getProductPrice(String productName) throws SQLException {
        String sql = "SELECT price FROM products WHERE name = ?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, productName);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    return resultSet.getDouble("price");
                } else {
                    throw new SQLException("Product not found");
                }
            }
        }
    }
}
