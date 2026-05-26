package com.pao.project.elearning.util;

import com.pao.project.elearning.exception.PersistenceException;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Properties;

public final class DatabaseConnection {
    private static final String PROPERTIES_RESOURCE =
            "/com/pao/project/elearning/resources/db.properties";
    private static DatabaseConnection instance;
    private Connection connection;

    private DatabaseConnection() {
        Properties properties = new Properties();
        try (InputStream input = DatabaseConnection.class.getResourceAsStream(PROPERTIES_RESOURCE)) {
            if (input == null) {
                throw new IOException("Nu gasesc resources/db.properties pentru proiectul elearning.");
            }
            properties.load(input);
            String url = properties.getProperty("db.url");
            if (url != null && url.startsWith("jdbc:sqlite:output/")) {
                new File("output").mkdirs();
            }
            connection = DriverManager.getConnection(url,
                    properties.getProperty("db.user", ""), properties.getProperty("db.password", ""));
            try (PreparedStatement statement = connection.prepareStatement("PRAGMA foreign_keys = ON")) {
                statement.execute();
            }
        } catch (IOException | SQLException ex) {
            throw new PersistenceException("Nu s-a putut initializa conexiunea la baza de date.", ex);
        }
    }

    public static synchronized DatabaseConnection getInstance() {
        if (instance == null || instance.isClosed()) {
            instance = new DatabaseConnection();
        }
        return instance;
    }

    public Connection getConnection() {
        return connection;
    }

    public void close() {
        try {
            if (connection != null && !connection.isClosed()) {
                connection.close();
            }
        } catch (SQLException ex) {
            throw new PersistenceException("Nu s-a putut inchide conexiunea.", ex);
        }
    }

    private boolean isClosed() {
        try {
            return connection == null || connection.isClosed();
        } catch (SQLException ex) {
            throw new PersistenceException("Nu s-a putut verifica starea conexiunii.", ex);
        }
    }
}
