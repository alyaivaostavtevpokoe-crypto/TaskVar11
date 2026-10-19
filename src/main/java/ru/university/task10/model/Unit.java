package ru.university.task10.model;

public class Unit {
    private int id;
    private String name;
    private int x;
    private int y;
    private int form;
    private int hp;
    private int speed;
    private int range;
    private int damage;

    public Unit(int id, String name, int x, int y, int form, int hp, int speed, int range, int damage) {
        this.id = id;
        this.name = name;
        this.x = x;
        this.y = y;
        this.form = form;
        this.hp = hp;
        this.speed = speed;
        this.range = range;
        this.damage = damage;
    }

    public int getId() { return id; }
    public String getName() { return name; }
    public int getX() { return x; }
    public int getY() { return y; }
    public int getForm() { return form; }
    public int getHp() { return hp; }
    public int getSpeed() { return speed; }
    public int getRange() { return range; }
    public int getDamage() { return damage; }

    public void setPos(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public void hit(int n) {
        hp -= n;
    }
}
