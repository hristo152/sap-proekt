package org.example.services;

import java.sql.*;
import java.util.InputMismatchException;
import java.util.Scanner;

public class ProductService {
    private Connection connection;
    private Scanner scanner;

    public ProductService(Connection connection){
        this.connection = connection;
    }
    public void displayAllProducts() {
        try {
            String sql = "SELECT * FROM products";
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(sql);

            System.out.println("All Products:");
            while (resultSet.next()) {
                double price = resultSet.getDouble("price");
                Integer discount = resultSet.getInt("discount");
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

    public void displayAllProductsWithDiscount() {
        try {
            String sql = "SELECT p.id, p.name, p.price, p.manufacturing_year, p.quality, p.quantity, p.description, p.discount " +
                    "FROM products p " +
                    "WHERE p.discount IS NOT NULL";
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(sql);


            System.out.println("Products with Discount:");
            while (resultSet.next()) {
                double price = resultSet.getDouble("price");
                Integer discount = resultSet.getInt("discount");
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

    public void addProduct() {
        try {
            scanner = new Scanner(System.in);
            System.out.println("Enter product details:");
            System.out.println("Name:");
            String name = scanner.nextLine();
            System.out.println("Price:");
            double price = scanner.nextDouble();
            System.out.println("Manufacturing Year:");
            int manufacturingYear = scanner.nextInt();
            System.out.println("Quantity:");
            int quantity = scanner.nextInt();
            System.out.println("Quality:");
            scanner.nextLine();
            String quality = scanner.nextLine();
            System.out.println("Description:");
            String description = scanner.nextLine();
            System.out.println("Discount:");
            int discount = scanner.nextInt();
            System.out.println("Category ID:");
            int categoryId = scanner.nextInt();

            Integer discountValue = null;
            if (discount > 0) {
                discountValue = discount;
            }

            String sql = "INSERT INTO products (name, price, manufacturing_year, quantity, quality, description, discount, category_id) " +
                    "VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
            try (PreparedStatement statement = connection.prepareStatement(sql)) {
                statement.setString(1, name);
                statement.setDouble(2, price);
                statement.setInt(3, manufacturingYear);
                statement.setInt(4, quantity);
                statement.setString(5, quality);
                statement.setString(6, description);
                if (discountValue == null) {
                    statement.setNull(7, Types.INTEGER);
                } else {
                    statement.setInt(7, discountValue);
                }
                statement.setInt(8, categoryId);
                statement.executeUpdate();
                System.out.println("Product added successfully.");
            }
        } catch (InputMismatchException e) {
            System.out.println("Invalid input. Please enter valid data.");
        } catch (SQLException e) {
            System.out.println("Error adding product: " + e.getMessage());
        }
    }

    public void editProduct() {
        try {
            System.out.println("Enter the ID of the product you want to edit:");
            scanner = new Scanner(System.in);
            int productId = scanner.nextInt();
            scanner.nextLine();

            System.out.println("Enter the attribute you want to edit (name, price, manufacturing year, quantity, quality, description, discount, category_id):");
            String attribute = scanner.nextLine();

            System.out.println("Enter the new value:");
            String newValue = scanner.nextLine();

            StringBuilder sqlBuilder = new StringBuilder("UPDATE products SET ");
            sqlBuilder.append(attribute).append(" = ? WHERE id = ?");

            String sql = sqlBuilder.toString();
            try (PreparedStatement statement = connection.prepareStatement(sql)) {
                statement.setString(1, newValue);
                statement.setInt(2, productId);
                int affectedRows = statement.executeUpdate();
                if (affectedRows > 0) {
                    System.out.println("Product updated successfully.");
                } else {
                    System.out.println("Product not found.");
                }
            }
        } catch (InputMismatchException e) {
            System.out.println("Invalid input. Please enter a valid ID.");
        } catch (SQLException e) {
            System.out.println("Error updating product: " + e.getMessage());
        }
    }

    public void removeProduct() {
        try {
            System.out.println("Enter the ID of the product you want to remove:");
            scanner = new Scanner(System.in);
            int productId = scanner.nextInt();
            scanner.nextLine();

            String sql = "DELETE FROM products WHERE id = ?";
            try (PreparedStatement statement = connection.prepareStatement(sql)) {
                statement.setInt(1, productId);
                int affectedRows = statement.executeUpdate();
                if (affectedRows > 0) {
                    System.out.println("Product removed successfully.");
                } else {
                    System.out.println("Product not found.");
                }
            }
        } catch (InputMismatchException e) {
            System.out.println("Invalid input. Please enter a valid ID.");
        } catch (SQLException e) {
            System.out.println("Error removing product: " + e.getMessage());
        }
    }
}
