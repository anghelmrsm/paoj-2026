package com.pao.project.magazin.util;

import com.pao.project.magazin.exception.PersistenceException;

import java.io.File;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.util.Properties;

public final class DatabaseConnection {
    private static DatabaseConnection instance;
    private final Connection connection;

    private DatabaseConnection() {
        try (InputStream input = DatabaseConnection.class.getResourceAsStream(
                "/com/pao/project/magazin/resources/db.properties")) {
            Properties properties = new Properties();
            properties.load(input);
            new File("output").mkdirs();
            connection = DriverManager.getConnection(properties.getProperty("db.url"));
            try (PreparedStatement statement = connection.prepareStatement("PRAGMA foreign_keys = ON")) {
                statement.execute();
            }
        } catch (Exception ex) {
            throw new PersistenceException("Conexiunea la baza de date nu a putut fi initializata.", ex);
        }
    }

    public static synchronized DatabaseConnection getInstance() {
        if (instance == null) {
            instance = new DatabaseConnection();
        }
        return instance;
    }

    public Connection getConnection() { return connection; }
}
