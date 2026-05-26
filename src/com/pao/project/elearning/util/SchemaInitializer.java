package com.pao.project.elearning.util;

import com.pao.project.elearning.exception.PersistenceException;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public final class SchemaInitializer {
    private static final String SCHEMA_RESOURCE = "/com/pao/project/elearning/schema.sql";

    private SchemaInitializer() {
    }

    public static void resetSchema() {
        try (InputStream input = SchemaInitializer.class.getResourceAsStream(SCHEMA_RESOURCE)) {
            if (input == null) {
                throw new IOException("Nu gasesc schema.sql pentru proiectul elearning.");
            }
            String schema = new String(input.readAllBytes(), StandardCharsets.UTF_8);
            for (String sql : schema.split(";")) {
                String statementSql = sql.trim();
                if (!statementSql.isEmpty()) {
                    try (PreparedStatement statement = DatabaseConnection.getInstance()
                            .getConnection().prepareStatement(statementSql)) {
                        statement.executeUpdate();
                    }
                }
            }
        } catch (IOException | SQLException ex) {
            throw new PersistenceException("Nu s-a putut initializa schema bazei de date.", ex);
        }
    }
}
