package com.pao.laboratory12.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Properties;

public final class DatabaseConnection {
    private static DatabaseConnection instance;
    private final Connection connection;

    private DatabaseConnection() throws IOException, SQLException {
        Properties properties = new Properties();
        InputStream source = getClass().getClassLoader().getResourceAsStream("db.properties");
        if (source == null) {
            source = new FileInputStream("src/com/pao/laboratory12/resources/db.properties");
        }
        try (InputStream input = source) {
            properties.load(input);
        }
        String url = properties.getProperty("db.url");
        if (url.startsWith("jdbc:sqlite:output/")) {
            new File("output").mkdirs();
        }
        connection = DriverManager.getConnection(url,
                properties.getProperty("db.user", ""), properties.getProperty("db.password", ""));
        try (PreparedStatement statement = connection.prepareStatement("PRAGMA foreign_keys = ON")) {
            statement.execute();
        }
    }

    public static synchronized DatabaseConnection getInstance() throws IOException, SQLException {
        if (instance == null || instance.connection.isClosed()) {
            instance = new DatabaseConnection();
        }
        return instance;
    }

    public Connection getConnection() { return connection; }

    public void close() throws SQLException { connection.close(); }
}
