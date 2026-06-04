package com.pao.project.magazin.repository;

import com.pao.project.magazin.exception.PersistenceException;
import com.pao.project.magazin.util.DatabaseConnection;

import java.sql.Connection;
import java.sql.SQLException;

abstract class JdbcRepositorySupport {
    protected Connection connection() { return DatabaseConnection.getInstance().getConnection(); }
    protected PersistenceException failure(String operatie, SQLException ex) {
        return new PersistenceException("Eroare JDBC la operatia: " + operatie, ex);
    }
}
