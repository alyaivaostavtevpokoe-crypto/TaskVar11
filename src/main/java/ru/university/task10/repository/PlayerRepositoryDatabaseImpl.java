package ru.university.task10.repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import javax.sql.DataSource;
import ru.university.task10.model.Player;

public class PlayerRepositoryDatabaseImpl implements PlayerRepository {
    private DataSource dataSource;

    public PlayerRepositoryDatabaseImpl(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    public void init() throws SQLException {
        try (Connection c = dataSource.getConnection(); Statement s = c.createStatement()) {
            s.executeUpdate("CREATE TABLE IF NOT EXISTS players (" +
                    "id INTEGER GENERATED ALWAYS AS IDENTITY PRIMARY KEY, " +
                    "name VARCHAR(80) NOT NULL)");
            s.executeUpdate("CREATE UNIQUE INDEX IF NOT EXISTS players_name_ci ON players (lower(name))");
        }
    }

    public ArrayList<Player> getAll() throws SQLException {
        ArrayList<Player> list = new ArrayList<>();
        try (Connection c = dataSource.getConnection();
             PreparedStatement p = c.prepareStatement("SELECT id, name FROM players ORDER BY id");
             ResultSet r = p.executeQuery()) {
            while (r.next()) {
                list.add(new Player(r.getInt("id"), r.getString("name")));
            }
        }
        return list;
    }

    public Player getById(int id) throws SQLException {
        try (Connection c = dataSource.getConnection();
             PreparedStatement p = c.prepareStatement("SELECT id, name FROM players WHERE id = ?")) {
            p.setInt(1, id);
            try (ResultSet r = p.executeQuery()) {
                if (r.next()) {
                    return new Player(r.getInt("id"), r.getString("name"));
                }
            }
        }
        return null;
    }


    public int add(String name) throws SQLException {
        try (Connection c = dataSource.getConnection()) {
            c.setAutoCommit(false);

            try {
                // Блокируем одновременное добавление игроков
                try (Statement s = c.createStatement()) {
                    s.execute("SELECT pg_advisory_xact_lock(12345)");
                }

                // Находим первый свободный ID
                int id;

                try (PreparedStatement p = c.prepareStatement(
                        "SELECT gs.candidate " +
                                "FROM generate_series(1, (SELECT COALESCE(MAX(id), 0) + 1 FROM players)) AS gs(candidate) " +
                                "WHERE NOT EXISTS (SELECT 1 FROM players WHERE players.id = gs.candidate) " +
                                "ORDER BY gs.candidate LIMIT 1");
                     ResultSet r = p.executeQuery()) {
                    r.next();
                    id = r.getInt(1);
                }

                // Добавляем игрока с найденным ID
                try (PreparedStatement p = c.prepareStatement(
                        "INSERT INTO players(id, name) OVERRIDING SYSTEM VALUE VALUES (?, ?)")) {
                    p.setInt(1, id);
                    p.setString(2, name);
                    p.executeUpdate();
                }

                c.commit();
                return id;

            } catch (SQLException e) {
                c.rollback();
                throw e;
            }
        }
    }


    public boolean rename(int id, String name) throws SQLException {
        try (Connection c = dataSource.getConnection();
             PreparedStatement p = c.prepareStatement("UPDATE players SET name = ? WHERE id = ?")) {
            p.setString(1, name);
            p.setInt(2, id);
            return p.executeUpdate() > 0;
        }
    }

    public boolean delete(int id) throws SQLException {
        try (Connection c = dataSource.getConnection();
             PreparedStatement p = c.prepareStatement("DELETE FROM players WHERE id = ?")) {
            p.setInt(1, id);
            return p.executeUpdate() > 0;
        }
    }
}
