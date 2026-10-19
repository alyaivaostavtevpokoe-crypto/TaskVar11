package ru.university.task10.repository;

import ru.university.task10.model.Unit;
import ru.university.task10.model.Wall;

import java.util.ArrayList;

public class BattleRepositoryInMemoryImpl implements BattleRepository {
    private ArrayList<Unit> units = new ArrayList<>();
    private ArrayList<Wall> walls = new ArrayList<>();

    public ArrayList<Unit> getUnits() {
        return new ArrayList<>(units);
    }

    public ArrayList<Wall> getWalls() {
        return new ArrayList<>(walls);
    }

    public Unit getUnit(int id) {
        for (Unit u : units) {
            if (u.getId() == id) {
                return u;
            }
        }
        return null;
    }

    public void addUnit(Unit u) {
        units.add(u);
    }

    public void addWall(Wall w) {
        walls.add(w);
    }

    public boolean removeUnit(int id) {
        for (int i = 0; i < units.size(); i++) {
            if (units.get(i).getId() == id) {
                units.remove(i);
                return true;
            }
        }
        return false;
    }

    public boolean removeWall(int id) {
        for (int i = 0; i < walls.size(); i++) {
            if (walls.get(i).getId() == id) {
                walls.remove(i);
                return true;
            }
        }
        return false;
    }
}
