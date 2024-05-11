package org.example.services;

import org.example.models.User;
import org.example.models.UserRole;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;

public class UserService {
    private Connection connection;
    private Scanner scanner;

    public UserService(Connection connection){
        this.connection = connection;
    }
    public User logInUser() {
        scanner = new Scanner(System.in);

        int attempts = 0;
        final int MAX_ATTEMPTS = 5;
        final int BLOCK_TIME_SECONDS = 10;
        int blockTime = BLOCK_TIME_SECONDS;

        while (attempts < MAX_ATTEMPTS) {
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

    public void signInUser() {
        scanner = new Scanner(System.in);
        System.out.println("Enter username:");
        String username = scanner.nextLine();
        System.out.println("Enter password:");
        String password = scanner.nextLine();
        System.out.println("Enter email:");
        String email = scanner.nextLine();
        System.out.println("Enter address:");
        String address = scanner.nextLine();

        if (doesUsernameExist(username)) {
            System.out.println("Username already exists. Please choose a different username.");
            return;
        }

        String sql = "INSERT INTO users (username, password, email, address, role) VALUES (?, ?, ?, ?, ?)";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, username);
            statement.setString(2, password);
            statement.setString(3, email);
            statement.setString(4, address);
            statement.setString(5, "User");
            statement.executeUpdate();
            System.out.println("User profile created successfully.");
        } catch (SQLException e) {
            System.out.println("Error creating user profile: " + e.getMessage());
        }
    }

    private boolean doesUsernameExist(String username) {
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
}
