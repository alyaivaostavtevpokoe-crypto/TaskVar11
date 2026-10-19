package ru.university.task10.controller;

import ru.university.task10.model.Unit;
import ru.university.task10.model.Wall;
import ru.university.task10.service.BattleService;

import java.util.Scanner;

public class CmdController {
    private BattleService service;
    private Scanner scanner;

    public CmdController(BattleService service, Scanner scanner) {
        this.service = service;
        this.scanner = scanner;
    }

    public void run() {
        while (true) {
            System.out.println("\n1 - показать поле");
            System.out.println("2 - добавить юнит");
            System.out.println("3 - добавить препятствие");
            System.out.println("4 - передвинуть юнит");
            System.out.println("5 - выстрелить");
            System.out.println("6 - список объектов");
            System.out.println("7 - убрать юнит");
            System.out.println("8 - убрать препятствие");
            System.out.println("0 - выход");
            System.out.print("выбор: ");
            if (!scanner.hasNextLine()) return;
            String n = scanner.nextLine();
            if (n.equals("1")) {
                showBoard();
            } else if (n.equals("2")) {
                addUnit();
            } else if (n.equals("3")) {
                addWall();
            } else if (n.equals("4")) {
                move();
            } else if (n.equals("5")) {
                shoot();
            } else if (n.equals("6")) {
                showAll();
            } else if (n.equals("7")) {
                int id = readNum("номер юнита: ");
                System.out.println(service.removeUnit(id) ? "убран" : "юнит не найден");
            } else if (n.equals("8")) {
                int id = readNum("номер препятствия: ");
                System.out.println(service.removeWall(id) ? "убрано" : "препятствие не найдено");
            } else if (n.equals("0")) {
                return;
            } else {
                System.out.println("нет такого пункта");
            }
        }
    }

    private void addUnit() {
        showForms();
        String name = readLine("название: ");
        int x = readNum("x от 0 до 9: ");
        int y = readNum("y от 0 до 9: ");
        int form = readNum("форма: ");
        int hp = readNum("здоровье: ");
        int speed = readNum("скорость (0 - стоит): ");
        int range = readNum("дальность выстрела: ");
        int damage = readNum("урон: ");
        int id = service.addUnit(name, x, y, form, hp, speed, range, damage);
        System.out.println(id == -1 ? "не удалось добавить" : "добавлен юнит U" + id);
    }

    private void addWall() {
        showForms();
        String name = readLine("название: ");
        int x = readNum("x от 0 до 9: ");
        int y = readNum("y от 0 до 9: ");
        int form = readNum("форма: ");
        int id = service.addWall(name, x, y, form);
        System.out.println(id == -1 ? "не удалось добавить" : "добавлено препятствие B" + id);
    }

    private void move() {
        showDirs();
        int id = readNum("номер юнита: ");
        int dir = readNum("направление: ");
        int steps = readNum("сколько клеток: ");
        System.out.println(service.move(id, dir, steps) ? "передвинулся" : "не удалось передвинуться");
    }

    private void shoot() {
        showDirs();
        int id = readNum("номер юнита: ");
        int dir = readNum("направление: ");
        System.out.println(service.shoot(id, dir));
    }

    private void showBoard() {
        System.out.print("    ");
        for (int x = 0; x < service.getWidth(); x++) {
            System.out.printf("%-4d", x);
        }
        System.out.println();
        for (int y = 0; y < service.getHeight(); y++) {
            System.out.printf("%-4d", y);
            for (int x = 0; x < service.getWidth(); x++) {
                System.out.printf("%-4s", service.cell(x, y));
            }
            System.out.println();
        }
        System.out.println("U - юнит, B - препятствие, . - пусто");
    }

    private void showAll() {
        System.out.println("юниты:");
        if (service.getUnits().isEmpty()) System.out.println("пока нет");
        for (Unit u : service.getUnits()) {
            System.out.println("U" + u.getId() + " " + u.getName() + " (" + u.getX() + "," + u.getY() +
                    ") форма " + u.getForm() + ", здоровье " + u.getHp() + ", скорость " + u.getSpeed() +
                    ", дальность " + u.getRange() + ", урон " + u.getDamage());
        }
        System.out.println("препятствия:");
        if (service.getWalls().isEmpty()) System.out.println("пока нет");
        for (Wall w : service.getWalls()) {
            System.out.println("B" + w.getId() + " " + w.getName() + " (" + w.getX() + "," + w.getY() +
                    ") форма " + w.getForm());
        }
    }

    private void showForms() {
        System.out.println("формы: 1 - одна клетка, 2 - две вправо, 3 - две вниз,");
        System.out.println("4 - квадрат 2x2, 5 - угол из трех клеток");
    }

    private void showDirs() {
        System.out.println("направления: 1 вверх, 2 вправо, 3 вниз, 4 влево,");
        System.out.println("5 вверх-вправо, 6 вниз-вправо, 7 вниз-влево, 8 вверх-влево");
    }

    private int readNum(String text) {
        try {
            return Integer.parseInt(readLine(text).trim());
        } catch (NumberFormatException e) {
            return -1;
        }
    }

    private String readLine(String text) {
        System.out.print(text);
        if (!scanner.hasNextLine()) return "";
        return scanner.nextLine();
    }
}
