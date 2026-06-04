package com.pao.project.magazin.util;

import com.pao.project.magazin.exception.PersistenceException;

import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.sql.PreparedStatement;

public final class SchemaInitializer {
    private SchemaInitializer() {}

    public static void resetSchema() {
        try (InputStream input = SchemaInitializer.class.getResourceAsStream("/com/pao/project/magazin/schema.sql")) {
            String schema = new String(input.readAllBytes(), StandardCharsets.UTF_8);
            for (String sql : schema.split(";")) {
                if (!sql.trim().isEmpty()) {
                    try (PreparedStatement statement = DatabaseConnection.getInstance()
                            .getConnection().prepareStatement(sql.trim())) {
                        statement.executeUpdate();
                    }
                }
            }
        } catch (Exception ex) {
            throw new PersistenceException("Schema magazinului nu a putut fi initializata.", ex);
        }
    }
}
