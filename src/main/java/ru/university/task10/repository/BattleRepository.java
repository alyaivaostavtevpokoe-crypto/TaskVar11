package ru.university.task10.repository;

import ru.university.task10.model.Unit;
import ru.university.task10.model.Wall;

import java.util.ArrayList;

public interface BattleRepository {
    ArrayList<Unit> getUnits();
    ArrayList<Wall> getWalls();
    Unit getUnit(int id);
    void addUnit(Unit u);
    void addWall(Wall w);
    boolean removeUnit(int id);
    boolean removeWall(int id);
}
