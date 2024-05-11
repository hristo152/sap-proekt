package org.example.services;

import org.example.models.User;
import org.example.models.UserRole;

import java.sql.Connection;
import java.util.Scanner;

public class MenusService {
    private Connection connection;
    private Scanner scanner;
    private UserService userService;
    private ProductService productService;
    private ShoppingcartService shoppingcartService;
    public MenusService(Connection connection){
        this.connection = connection;
        userService = new UserService(connection);
        productService = new ProductService(connection);
        shoppingcartService = new ShoppingcartService(connection);
    }

    public void handleUserChoice() {
        System.out.println("Welcome to the Online Vintage Shop!");
        System.out.println("1. Log In");
        System.out.println("2. Sign Up");
        scanner = new Scanner(System.in);
        int choice = scanner.nextInt();

        switch (choice) {
            case 1:
                handleLogin();
                break;
            case 2:
                handleSignUp();
                break;
            default:
                System.out.println("Invalid choice.");
        }
    }

    private void handleLogin() {
        User loggedInUser = userService.logInUser();

        if (loggedInUser != null) {
            displayMenu(loggedInUser);
        } else {
            System.out.println("Login failed. Exiting program.");
        }
    }

    private void handleSignUp() {
        userService.signInUser();
        User signedUpUser = userService.logInUser();

        if (signedUpUser != null) {
            displayMenu(signedUpUser);
        } else {
            System.out.println("Sign up failed. Exiting program.");
        }
    }

    private void displayUserMenu() {
        System.out.println();
        System.out.println("Please choose from the menu:");
        System.out.println("1. View All Products");
        System.out.println("2. View All Products With Discount");
        System.out.println("3. Add to Shopping cart");
        System.out.println("4. View Shopping cart");
        System.out.println("5. Exit");
    }

    private void displayAdminMenu() {
        System.out.println();
        System.out.println("Please choose from the menu:");
        System.out.println("1. View All Products");
        System.out.println("2. View All Products With Discount");
        System.out.println("3. Add New Product");
        System.out.println("4. Edit Product");
        System.out.println("5. Delete Product");
        System.out.println("6. Exit");
    }

    public void displayMenu(User user) {
        scanner = new Scanner(System.in);
        int choice;

        if (user != null) {
            if (user.getRole() == UserRole.USER) {
                while(true){
                    displayUserMenu();
                    choice = scanner.nextInt();
                    userActions(choice, user.getId());
                }
            } else if (user.getRole() == UserRole.ADMIN) {
                while(true){
                    displayAdminMenu();
                    choice = scanner.nextInt();
                    adminActions(choice);
                }
            }
        } else {
            System.out.println("Failed to retrieve user details. Exiting program.");
        }
    }

    private void userActions(int choice, int userId){
        switch (choice) {
            case 1:
                productService.displayAllProducts();
                break;
            case 2:
                productService.displayAllProductsWithDiscount();
                break;
            case 3:
                shoppingcartService.addToCart(userId);
                break;
            case 4:
                shoppingcartService.viewCart(userId);
                break;
            case 5:
                System.out.println("Exiting program.");
                System.exit(0);
                break;
            default:
                System.out.println("Invalid choice.");
        }
    }

    private void adminActions(int choice){
        switch (choice) {
            case 1:
                productService.displayAllProducts();
                break;
            case 2:
                productService.displayAllProductsWithDiscount();
                break;
            case 3:
                productService.addProduct();
                break;
            case 4:
                productService.editProduct();
                break;
            case 5:
                productService.removeProduct();
                break;
            case 6:
                System.out.println("Exiting program.");
                System.exit(0);
                break;
            default:
                System.out.println("Invalid choice.");
        }
    }
}
