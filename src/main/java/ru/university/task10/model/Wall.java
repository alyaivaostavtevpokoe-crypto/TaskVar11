package ru.university.task10.model;

public class Wall {
    private int id;
    private String name;
    private int x;
    private int y;
    private int form;

    public Wall(int id, String name, int x, int y, int form) {
        this.id = id;
        this.name = name;
        this.x = x;
        this.y = y;
        this.form = form;
    }

    public int getId() { return id; }
    public String getName() { return name; }
    public int getX() { return x; }
    public int getY() { return y; }
    public int getForm() { return form; }
}
