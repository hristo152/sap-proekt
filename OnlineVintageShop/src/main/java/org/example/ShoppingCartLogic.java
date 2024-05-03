package org.example;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class ShoppingCartLogic {
    private List<ShoppingCartItem> cartItems;
    private ShoppingCart shoppingCart;
    private int userId;

    public ShoppingCartLogic(int userId) {
        this.userId = userId;
        cartItems = new ArrayList<>();
        // Assuming shopping cart ID is generated or fetched from the database
        shoppingCart = new ShoppingCart(1, 0.0, userId);
    }
}