package ru.university.task10;

import java.util.Scanner;
import java.sql.SQLException;
import org.postgresql.ds.PGSimpleDataSource;
import ru.university.task10.controller.CmdController;
import ru.university.task10.repository.BattleRepository;
import ru.university.task10.repository.BattleRepositoryInMemoryImpl;
import ru.university.task10.repository.PlayerRepositoryDatabaseImpl;
import ru.university.task10.service.BattleService;
import ru.university.task10.service.PlayerService;

public class Main {
    public static void main(String[] args) {
        PGSimpleDataSource ds = new PGSimpleDataSource();
        ds.setUrl(env("DB_URL", "jdbc:postgresql://localhost:5432/taskvar10"));
        ds.setUser(env("DB_USER", "postgres"));
        ds.setPassword(env("DB_PASSWORD", ""));
        PlayerRepositoryDatabaseImpl playerRepo = new PlayerRepositoryDatabaseImpl(ds);
        try {
            playerRepo.init();
        } catch (SQLException e) {
            System.out.println("не удалось подключиться к базе игроков: " + e.getMessage());
            return;
        }
        BattleRepository repo = new BattleRepositoryInMemoryImpl();
        BattleService service = new BattleService(repo);
        PlayerService players = new PlayerService(playerRepo);
        CmdController controller = new CmdController(service, players, new Scanner(System.in));
        controller.run();
    }

    private static String env(String key, String defaultValue) {
        String value = System.getenv(key);
        return value == null || value.isEmpty() ? defaultValue : value;
    }
}
