package org.example;

import java.io.IOException;
import java.sql.*;
import java.util.*;

public class Main {
    private static Connection connection;
    private static Scanner scanner;
    //private static ShoppingCart shoppingCart; // Declare shoppingCart as a field
    //private static List<ShoppingCartItem> cartItems; // Declare cartItems as a field

    private static void connectToDatabase() {
        try {
            connection = DriverManager.getConnection(
                    "jdbc:mysql://localhost:3306/onlinevintageshop", "root", "hstod03230");
            if (connection != null) {
                System.out.println("Connected to the database!");
            }
        } catch (SQLException e) {
            System.out.println("Failed to connect to the database: " + e.getMessage());
        }
    }

    /*private static User logInUser() {
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
    }*/
    private static void signInUser() {
        scanner = new Scanner(System.in);
        System.out.println("Enter username:");
        String username = scanner.nextLine();
        System.out.println("Enter password:");
        String password = scanner.nextLine();
        System.out.println("Enter email:");
        String email = scanner.nextLine();
        System.out.println("Enter address:");
        String address = scanner.nextLine();

        // Check if the username already exists
        if (isUsernameExists(username)) {
            System.out.println("Username already exists. Please choose a different username.");
            return;
        }

        // Insert the user into the database
        String sql = "INSERT INTO users (username, password, email, address) VALUES (?, ?, ?, ?)";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, username);
            statement.setString(2, password);
            statement.setString(3, email);
            statement.setString(4, address);
            statement.executeUpdate();
            System.out.println("User profile created successfully.");
        } catch (SQLException e) {
            System.out.println("Error creating user profile: " + e.getMessage());
        }
    }

    private static boolean isUsernameExists(String username) {
        String sql = "SELECT COUNT(*) AS count FROM users WHERE username = ?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, username);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    int count = resultSet.getInt("count");
                    return count > 0;
                }
            }
        } catch (SQLException e) {
            System.out.println("Error checking username existence: " + e.getMessage());
        }
        return false;
    }

    private static User logInUser() {
        scanner = new Scanner(System.in);
        System.out.println("Enter username:");
        String username = scanner.nextLine();
        System.out.println("Enter password:");

        int attempts = 0;
        final int MAX_ATTEMPTS = 5;
        final int BLOCK_TIME_SECONDS = 10;
        int blockTime = BLOCK_TIME_SECONDS;

        while (attempts < MAX_ATTEMPTS) {
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
                    attempts++;
                    if (attempts < MAX_ATTEMPTS) {
                        System.out.println("Incorrect username or password. Try again.");
                    } else {
                        System.out.println("Maximum attempts reached. Please wait for " + blockTime + " seconds before trying again.");
                        Thread.sleep(blockTime * 1000);
                        attempts = 0;
                        blockTime += BLOCK_TIME_SECONDS;
                        System.out.println("Enter password:");
                    }
                }
            } catch (SQLException e) {
                System.out.println("Error logging in: " + e.getMessage());
                return null;
            } catch (InterruptedException e) {
                System.out.println("Thread interrupted: " + e.getMessage());
                Thread.currentThread().interrupt();
            }
        }
        System.out.println("Maximum attempts reached. Exiting program.");
        System.exit(0);
        return null;
    }

    private static void displayUserMenu() {
        System.out.println("Please choose from the menu:");
        System.out.println("1. View All Products");
        System.out.println("2. View All Products With Discount");
        System.out.println("3. Add to Shopping cart");
        System.out.println("4. View Shopping cart");
        System.out.println("5. Exit");
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

    private static void addProduct(Product product) {
        try {
            String sql = "INSERT INTO products (id, name, price, manufacturing_year, quantity, quality, description, category_id) " +
                    "VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
            try (PreparedStatement statement = connection.prepareStatement(sql)) {
                statement.setInt(1, product.getId());
                statement.setString(2, product.getName());
                statement.setDouble(3, product.getPrice());
                statement.setInt(4, product.getManufacturingYear());
                statement.setInt(5, product.getQuantity());
                statement.setInt(6, product.getQuality());
                statement.setString(7, product.getDescription());
                statement.setInt(8, product.getCategoryId());
                statement.executeUpdate();
                System.out.println("Product added successfully.");
            }
        } catch (SQLException e) {
            System.out.println("Error adding product: " + e.getMessage());
        }
    }

    private static void editProduct(int productId, Map<String, Object> updates) {
        try {
            StringBuilder sqlBuilder = new StringBuilder("UPDATE products SET ");
            List<Object> values = new ArrayList<>();

            for (String attribute : updates.keySet()) {
                sqlBuilder.append(attribute).append(" = ?, ");
                values.add(updates.get(attribute));
            }

            sqlBuilder.delete(sqlBuilder.length() - 2, sqlBuilder.length());

            sqlBuilder.append(" WHERE id = ?");
            values.add(productId);

            String sql = sqlBuilder.toString();
            try (PreparedStatement statement = connection.prepareStatement(sql)) {
                int index = 1;
                for (Object value : values) {
                    statement.setObject(index++, value);
                }
                int affectedRows = statement.executeUpdate();
                if (affectedRows > 0) {
                    System.out.println("Product updated successfully.");
                } else {
                    System.out.println("Product not found.");
                }
            }
        } catch (SQLException e) {
            System.out.println("Error updating product: " + e.getMessage());
        }
    }

    private static void removeProduct(int productId) {
        try {
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
        } catch (SQLException e) {
            System.out.println("Error removing product: " + e.getMessage());
        }
    }

    private static void addItemToCart(int productId, int quantity) {
        try {
            double price = getProductPrice(productId);

            String sql = "INSERT INTO shopping_cart_item (product_id, quantity, price) VALUES (?, ?, ?)";
            try (PreparedStatement statement = connection.prepareStatement(sql)) {
                statement.setInt(1, productId);
                statement.setInt(2, quantity);
                statement.setDouble(3, price);
                statement.executeUpdate();
            }

            updateTotalSum();
        } catch (SQLException e) {
            System.out.println("Error adding item to cart: " + e.getMessage());
        }
    }

    private static double getProductPrice(int productId) throws SQLException {
        String sql = "SELECT price FROM products WHERE id = ?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, productId);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    return resultSet.getDouble("price");
                } else {
                    throw new SQLException("Product not found");
                }
            }
        }
    }

    private static void updateTotalSum() {
        try {
            String sql = "SELECT SUM(price * quantity) AS total_sum FROM shopping_cart_item";
            try (Statement statement = connection.createStatement()) {
                try (ResultSet resultSet = statement.executeQuery(sql)) {
                    if (resultSet.next()) {
                        double totalSum = resultSet.getDouble("total_sum");

                        String updateSql = "UPDATE shopping_carts SET total_sum = ?";
                        try (PreparedStatement updateStatement = connection.prepareStatement(updateSql)) {
                            updateStatement.setDouble(1, totalSum);
                            updateStatement.executeUpdate();
                        }
                    }
                }
            }
        } catch (SQLException e) {
            System.out.println("Error updating total sum: " + e.getMessage());
        }
    }

    private static void viewCart() {
        try {
            String sql = "SELECT * FROM shopping_cart_item";
            try (Statement statement = connection.createStatement()) {
                try (ResultSet resultSet = statement.executeQuery(sql)) {
                    System.out.println("Shopping Cart:");
                    while (resultSet.next()) {
                        int productId = resultSet.getInt("product_id");
                        int quantity = resultSet.getInt("quantity");
                        double price = resultSet.getDouble("price");
                        System.out.println("Product ID: " + productId + ", Quantity: " + quantity + ", Price: $" + price);
                    }
                }
            }
            String totalSumSql = "SELECT total_sum FROM shopping_cart";
            try (Statement totalSumStatement = connection.createStatement()) {
                try (ResultSet totalSumResultSet = totalSumStatement.executeQuery(totalSumSql)) {
                    if (totalSumResultSet.next()) {
                        double totalSum = totalSumResultSet.getDouble("total_sum");
                        System.out.println("Total Sum: $" + totalSum);
                    }
                }
            }
        } catch (SQLException e) {
            System.out.println("Error viewing cart: " + e.getMessage());
        }
    }
    /*public static void main(String[] args) throws IOException {
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
                    case 3:
                        System.out.println("Enter product ID to add to cart:");
                        int productId = scanner.nextInt();
                        System.out.println("Enter quantity:");
                        int quantity = scanner.nextInt();
                        addItemToCart(productId, quantity);
                        break;
                    case 4:
                        System.out.println("Exiting program.");
                        System.exit(0); // Exit the program
                        break;
                    default:
                        System.out.println("Invalid choice.");

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
                    case 6:
                        System.out.println("Exiting program.");
                        System.exit(0); // Exit the program
                        break;
                    default:
                        System.out.println("Invalid choice.");
                }
            }
        } else {
            System.out.println("Login failed. Exiting program.");
        }
    }*/

    private static void logicForAddProduct() {
        scanner = new Scanner(System.in);
        System.out.println("Enter product details:");
        System.out.println("ID:");
        int id = scanner.nextInt();
        scanner.nextLine();
        System.out.println("Name:");
        String name = scanner.nextLine();
        System.out.println("Price:");
        double price = scanner.nextDouble();
        System.out.println("Manufacturing Year:");
        int manufacturingYear = scanner.nextInt();
        System.out.println("Quantity:");
        int quantity = scanner.nextInt();
        System.out.println("Quality:");
        int quality = scanner.nextInt();
        scanner.nextLine();
        System.out.println("Description:");
        String description = scanner.nextLine();
        System.out.println("Category ID:");
        int categoryId = scanner.nextInt();

        Product product = new Product(id, name, price, manufacturingYear, quantity, quality, description, categoryId);
        addProduct(product);
    }

    private static void logicFoEditProduct() {
        try {
            System.out.println("Enter the ID of the product you want to edit:");
            scanner = new Scanner(System.in);
            int productId = scanner.nextInt();
            scanner.nextLine();

            System.out.println("Enter the attribute you want to edit (name, price, manufacturing_year, quantity, quality, description, category_id):");
            String attribute = scanner.nextLine();

            System.out.println("Enter the new value:");
            String newValue = scanner.nextLine();

            Map<String, Object> updates = new HashMap<>();
            updates.put(attribute, newValue);

            editProduct(productId, updates);
        } catch (InputMismatchException e) {
            System.out.println("Invalid input. Please enter a valid ID.");
        }
    }

    private static void logicForRemoveProduct() {
        try {
            System.out.println("Enter the ID of the product you want to remove:");
            scanner = new Scanner(System.in);
            int productId = scanner.nextInt();

            removeProduct(productId);
        } catch (InputMismatchException e) {
            System.out.println("Invalid input. Please enter a valid ID.");
        }
    }

    private static void logicForAddToCart() {
        System.out.println("Enter product ID to add to cart:");
        int productId = scanner.nextInt();
        System.out.println("Enter quantity:");
        int quantity = scanner.nextInt();
        addItemToCart(productId, quantity);
    }

    public static void main(String[] args) throws IOException {
        int choice;
        connectToDatabase();

        System.out.println("Welcome to the Online Vintage Shop!");
        System.out.println("1. Log In");
        System.out.println("2. Sign Up");
        scanner = new Scanner(System.in);
        choice = scanner.nextInt();

        switch (choice) {
            case 1:
                User loggedInUser = logInUser();
                if (loggedInUser != null) {
                    if (loggedInUser.getRole() == UserRole.USER) {
                        displayUserMenu();
                        choice = scanner.nextInt();

                        switch (choice) {
                            case 1:
                                displayAllProducts();
                                break;
                            case 2:
                                displayAllProductsWithDiscount();
                                break;
                            case 3:
                                logicForAddToCart();
                                break;
                            case 4:
                                viewCart();
                                break;
                            case 5:
                                System.out.println("Exiting program.");
                                System.exit(0);
                                break;
                            default:
                                System.out.println("Invalid choice.");
                        }
                    } else if (loggedInUser.getRole() == UserRole.ADMIN) {
                        displayAdminMenu();
                        choice = scanner.nextInt();

                        switch (choice) {
                            case 1:
                                displayAllProducts();
                                break;
                            case 2:
                                displayAllProductsWithDiscount();
                                break;
                            case 3:
                                logicForAddProduct();
                                break;
                            case 4:
                                logicFoEditProduct();
                                break;
                            case 5:
                                logicForRemoveProduct();
                                break;
                            case 6:
                                System.out.println("Exiting program.");
                                System.exit(0);
                                break;
                            default:
                                System.out.println("Invalid choice.");
                        }
                    }
                } else {
                    System.out.println("Login failed. Exiting program.");
                }
                break;
            case 2:
                signInUser();
                User signedUpUser = logInUser();
                if (signedUpUser != null) {
                    if (signedUpUser.getRole() == UserRole.USER) {
                        displayUserMenu();
                        choice = scanner.nextInt();

                        switch (choice) {
                            case 1:
                                displayAllProducts();
                                break;
                            case 2:
                                displayAllProductsWithDiscount();
                                break;
                            case 3:
                                logicForAddToCart();
                                break;
                            case 4:
                                viewCart();
                                break;
                            case 5:
                                System.out.println("Exiting program.");
                                System.exit(0);
                                break;
                            default:
                                System.out.println("Invalid choice.");
                        }
                    } else if (signedUpUser.getRole() == UserRole.ADMIN) {
                        displayAdminMenu();
                        choice = scanner.nextInt();

                        switch (choice) {
                            case 1:
                                displayAllProducts();
                                break;
                            case 2:
                                displayAllProductsWithDiscount();
                                break;
                            case 3:
                                logicForAddProduct();
                                break;
                            case 4:
                                logicFoEditProduct();
                                break;
                            case 5:
                                logicForRemoveProduct();
                                break;
                            case 6:
                                System.out.println("Exiting program.");
                                System.exit(0);
                                break;
                            default:
                                System.out.println("Invalid choice.");
                        }
                    }
                } else {
                    System.out.println("Sign up failed. Exiting program.");
                }
                break;
            default:
                System.out.println("Invalid choice.");
        }
    }
}
