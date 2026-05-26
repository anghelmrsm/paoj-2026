package com.pao.laboratory14.exercise2.repository;

import com.pao.laboratory14.exercise1.TipBilet;
import com.pao.laboratory14.exercise2.model.Eveniment;
import com.pao.laboratory14.exercise2.util.DatabaseConnection;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class EvenimentRepository implements Repository<Eveniment, Integer> {
    private Connection connection() throws SQLException {
        try {
            return DatabaseConnection.getInstance().getConnection();
        } catch (IOException ex) {
            throw new SQLException(ex);
        }
    }

    public void initSchema() throws SQLException {
        try (PreparedStatement drop = connection().prepareStatement("DROP TABLE IF EXISTS evenimente")) {
            drop.executeUpdate();
        }
        try (PreparedStatement create = connection().prepareStatement(
                "CREATE TABLE evenimente (id INTEGER PRIMARY KEY AUTOINCREMENT, "
                        + "nume TEXT NOT NULL, data TEXT NOT NULL, capacitate INTEGER, tip TEXT)")) {
            create.executeUpdate();
        }
    }

    @Override
    public void save(Eveniment event) throws SQLException {
        String sql = "INSERT INTO evenimente (nume, data, capacitate, tip) VALUES (?, ?, ?, ?)";
        try (PreparedStatement statement = connection().prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            statement.setString(1, event.getNume());
            statement.setString(2, event.getData());
            statement.setInt(3, event.getCapacitate());
            statement.setString(4, event.getTip().name());
            statement.executeUpdate();
            try (ResultSet keys = statement.getGeneratedKeys()) {
                if (keys.next()) {
                    event.setId(keys.getInt(1));
                }
            }
        }
    }

    @Override
    public Optional<Eveniment> findById(Integer id) throws SQLException {
        try (PreparedStatement statement = connection().prepareStatement(
                "SELECT id, nume, data, capacitate, tip FROM evenimente WHERE id = ?")) {
            statement.setInt(1, id);
            try (ResultSet result = statement.executeQuery()) {
                return result.next() ? Optional.of(map(result)) : Optional.empty();
            }
        }
    }

    @Override
    public List<Eveniment> findAll() throws SQLException {
        List<Eveniment> events = new ArrayList<>();
        try (PreparedStatement statement = connection().prepareStatement(
                "SELECT id, nume, data, capacitate, tip FROM evenimente ORDER BY id");
             ResultSet result = statement.executeQuery()) {
            while (result.next()) {
                events.add(map(result));
            }
        }
        return events;
    }

    @Override
    public void update(Eveniment event) throws SQLException {
        try (PreparedStatement statement = connection().prepareStatement(
                "UPDATE evenimente SET nume = ?, data = ?, capacitate = ?, tip = ? WHERE id = ?")) {
            statement.setString(1, event.getNume());
            statement.setString(2, event.getData());
            statement.setInt(3, event.getCapacitate());
            statement.setString(4, event.getTip().name());
            statement.setInt(5, event.getId());
            statement.executeUpdate();
        }
    }

    @Override
    public void delete(Integer id) throws SQLException {
        deleteImpl(id);
    }

    public int deleteImpl(int id) throws SQLException {
        try (PreparedStatement statement = connection().prepareStatement(
                "DELETE FROM evenimente WHERE id = ?")) {
            statement.setInt(1, id);
            return statement.executeUpdate();
        }
    }

    public int count() throws SQLException {
        try (PreparedStatement statement = connection().prepareStatement("SELECT COUNT(*) FROM evenimente");
             ResultSet result = statement.executeQuery()) {
            result.next();
            return result.getInt(1);
        }
    }

    private Eveniment map(ResultSet result) throws SQLException {
        Eveniment event = new Eveniment(result.getString("nume"), result.getString("data"),
                result.getInt("capacitate"), TipBilet.valueOf(result.getString("tip")));
        event.setId(result.getInt("id"));
        return event;
    }
}
