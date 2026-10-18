package ru.university.task10;

import java.util.Scanner;
import ru.university.task10.controller.CmdController;
import ru.university.task10.repository.BattleRepository;
import ru.university.task10.repository.BattleRepositoryInMemoryImpl;
import ru.university.task10.service.BattleService;

public class Main {
    public static void main(String[] args) {
        BattleRepository repo = new BattleRepositoryInMemoryImpl();
        BattleService service = new BattleService(repo);
        CmdController controller = new CmdController(service, new Scanner(System.in));
        controller.run();
    }
}