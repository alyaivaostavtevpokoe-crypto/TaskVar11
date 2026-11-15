package ru.university.task10.repository;

import java.sql.SQLException;
import java.util.ArrayList;
import ru.university.task10.model.Player;

public interface PlayerRepository {
    ArrayList<Player> getAll() throws SQLException;
    Player getById(int id) throws SQLException;
    int add(String name) throws SQLException;
    boolean rename(int id, String name) throws SQLException;
    boolean delete(int id) throws SQLException;
}
