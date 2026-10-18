package ru.university.task10.service;

import ru.university.task10.model.Unit;
import ru.university.task10.model.Wall;
import ru.university.task10.repository.BattleRepository;

import java.util.ArrayList;

public class BattleService {
    private BattleRepository repo;
    private int nextUnit = 1;
    private int nextWall = 1;
    private int width = 10;
    private int height = 10;
    private int[] vx = {0, 0, 1, 0, -1, 1, 1, -1, -1};
    private int[] vy = {0, -1, 0, 1, 0, -1, 1, 1, -1};

    public BattleService(BattleRepository repo) {
        this.repo = repo;
    }

    public int getWidth() { return width; }
    public int getHeight() { return height; }
    public ArrayList<Unit> getUnits() { return repo.getUnits(); }
    public ArrayList<Wall> getWalls() { return repo.getWalls(); }

    public int addUnit(String name, int x, int y, int form, int hp, int speed, int range, int damage) {
        if (name == null || name.trim().isEmpty() || hp < 1 || speed < 0 || range < 1 || damage < 1 ||
                !free(x, y, form, 0)) {
            return -1;
        }
        repo.addUnit(new Unit(nextUnit, name.trim(), x, y, form, hp, speed, range, damage));
        return nextUnit++;
    }

    public int addWall(String name, int x, int y, int form) {
        if (name == null || name.trim().isEmpty() || !free(x, y, form, 0)) {
            return -1;
        }
        repo.addWall(new Wall(nextWall, name.trim(), x, y, form));
        return nextWall++;
    }

    public boolean removeUnit(int id) {
        return repo.removeUnit(id);
    }

    public boolean removeWall(int id) {
        return repo.removeWall(id);
    }

    public boolean move(int id, int dir, int steps) {
        Unit u = repo.getUnit(id);
        if (u == null || dir < 1 || dir > 8 || steps < 1 || steps > u.getSpeed()) {
            return false;
        }
        int x = u.getX();
        int y = u.getY();
        for (int i = 0; i < steps; i++) {
            x += vx[dir];
            y += vy[dir];
            if (!free(x, y, u.getForm(), id)) {
                return false;
            }
        }
        u.setPos(x, y);
        return true;
    }

    public String shoot(int id, int dir) {
        Unit u = repo.getUnit(id);
        if (u == null || dir < 1 || dir > 8) {
            return "неверный номер юнита или направление";
        }
        int[][] form = shape(u.getForm());
        int x = u.getX();
        int y = u.getY();
        int best = -100;
        for (int[] c : form) {
            int n = c[0] * vx[dir] + c[1] * vy[dir];
            if (n > best) {
                best = n;
                x = u.getX() + c[0];
                y = u.getY() + c[1];
            }
        }
        for (int i = 0; i < u.getRange(); i++) {
            x += vx[dir];
            y += vy[dir];
            if (x < 0 || y < 0 || x >= width || y >= height) {
                break;
            }
            Wall w = wallAt(x, y);
            if (w != null) {
                return "снаряд попал в препятствие " + w.getName();
            }
            Unit other = unitAt(x, y);
            if (other != null) {
                other.hit(u.getDamage());
                if (other.getHp() <= 0) {
                    repo.removeUnit(other.getId());
                    return "юнит " + other.getName() + " уничтожен";
                }
                return "попадание в " + other.getName() + ", осталось здоровья: " + other.getHp();
            }
        }
        return "никого не задело";
    }

    public String cell(int x, int y) {
        Unit u = unitAt(x, y);
        if (u != null) {
            return "U" + u.getId();
        }
        Wall w = wallAt(x, y);
        if (w != null) {
            return "B" + w.getId();
        }
        return ".";
    }

    private boolean free(int x, int y, int form, int ignore) {
        int[][] cells = shape(form);
        if (cells == null) {
            return false;
        }
        for (int[] c : cells) {
            int xx = x + c[0];
            int yy = y + c[1];
            if (xx < 0 || yy < 0 || xx >= width || yy >= height || wallAt(xx, yy) != null) {
                return false;
            }
            Unit u = unitAt(xx, yy);
            if (u != null && u.getId() != ignore) {
                return false;
            }
        }
        return true;
    }

    private Unit unitAt(int x, int y) {
        for (Unit u : repo.getUnits()) {
            if (hasCell(u.getX(), u.getY(), u.getForm(), x, y)) {
                return u;
            }
        }
        return null;
    }

    private Wall wallAt(int x, int y) {
        for (Wall w : repo.getWalls()) {
            if (hasCell(w.getX(), w.getY(), w.getForm(), x, y)) {
                return w;
            }
        }
        return null;
    }

    private boolean hasCell(int x, int y, int form, int xx, int yy) {
        for (int[] c : shape(form)) {
            if (x + c[0] == xx && y + c[1] == yy) {
                return true;
            }
        }
        return false;
    }

    private int[][] shape(int form) {
        if (form == 1) return new int[][]{{0, 0}};
        if (form == 2) return new int[][]{{0, 0}, {1, 0}};
        if (form == 3) return new int[][]{{0, 0}, {0, 1}};
        if (form == 4) return new int[][]{{0, 0}, {1, 0}, {0, 1}, {1, 1}};
        if (form == 5) return new int[][]{{0, 0}, {0, 1}, {1, 1}};
        return null;
    }
}
