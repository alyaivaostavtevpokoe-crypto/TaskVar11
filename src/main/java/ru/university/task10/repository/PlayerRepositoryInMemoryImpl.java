package ru.university.task10.repository;

import java.util.ArrayList;
import ru.university.task10.model.Player;

public class PlayerRepositoryInMemoryImpl implements PlayerRepository {
    private ArrayList<Player> players = new ArrayList<>();
    private int nextId = 1;

    public ArrayList<Player> getAll() {
        return new ArrayList<>(players);
    }

    public Player getById(int id) {
        for (Player p : players) {
            if (p.getId() == id) return p;
        }
        return null;
    }

    public int add(String name) {
        players.add(new Player(nextId, name));
        return nextId++;
    }

    public boolean rename(int id, String name) {
        for (int i = 0; i < players.size(); i++) {
            if (players.get(i).getId() == id) {
                players.set(i, new Player(id, name));
                return true;
            }
        }
        return false;
    }

    public boolean delete(int id) {
        for (int i = 0; i < players.size(); i++) {
            if (players.get(i).getId() == id) {
                players.remove(i);
                return true;
            }
        }
        return false;
    }
}
