package com.pao.project.elearning.repository;

import com.pao.project.elearning.exception.PersistenceException;
import com.pao.project.elearning.util.DatabaseConnection;

import java.sql.Connection;
import java.sql.SQLException;

abstract class JdbcRepositorySupport {
    protected Connection connection() {
        return DatabaseConnection.getInstance().getConnection();
    }

    protected PersistenceException failure(String operation, SQLException cause) {
        return new PersistenceException("Eroare JDBC la operatia: " + operation, cause);
    }
}
