package org.example;

import org.example.services.MenusService;

import java.io.IOException;
import java.sql.*;

public class Main {
    private static MenusService menusService;
    private static Connection connection;

    public static void main(String[] args) throws IOException {
        connection = DBConnector.connectToDatabase();

        menusService = new MenusService(connection);
        menusService.handleUserChoice();
    }
}

