package ru.university.task10.service;

import java.sql.SQLException;
import java.util.ArrayList;
import ru.university.task10.model.Player;
import ru.university.task10.repository.PlayerRepository;

public class PlayerService {
    private PlayerRepository repo;

    public PlayerService(PlayerRepository repo) {
        this.repo = repo;
    }

    public ArrayList<Player> getAll() throws SQLException {
        return repo.getAll();
    }

    public Player getById(int id) throws SQLException {
        return repo.getById(id);
    }

    public int add(String name) throws SQLException {
        if (!valid(name) || taken(name, 0)) return -1;
        return repo.add(name.trim());
    }

    public boolean rename(int id, String name) throws SQLException {
        if (repo.getById(id) == null || !valid(name) || taken(name, id)) return false;
        return repo.rename(id, name.trim());
    }

    public boolean delete(int id) throws SQLException {
        return repo.delete(id);
    }

    private boolean valid(String name) {
        return name != null && !name.trim().isEmpty() && name.trim().length() <= 80;
    }

    private boolean taken(String name, int id) throws SQLException {
        for (Player p : repo.getAll()) {
            if (p.getId() != id && p.getName().equalsIgnoreCase(name.trim())) return true;
        }
        return false;
    }
}
