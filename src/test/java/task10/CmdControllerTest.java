package task10;

import org.junit.jupiter.api.Test;
import ru.university.task10.controller.CmdController;
import ru.university.task10.repository.BattleRepositoryInMemoryImpl;
import ru.university.task10.service.BattleService;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.Scanner;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class CmdControllerTest {
    @Test
    public void menuAddsShowsAndRemovesUnit() throws Exception {
        String input = String.join("\n", "2", "танк", "1", "1", "1", "5", "1", "3", "2",
                "6", "1", "7", "1", "6", "0") + "\n";
        PrintStream old = System.out;
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        try {
            System.setOut(new PrintStream(bytes, true, "UTF-8"));
            CmdController c = new CmdController(new BattleService(new BattleRepositoryInMemoryImpl()),
                    new Scanner(input));
            c.run();
        } finally {
            System.setOut(old);
        }
        String text = bytes.toString("UTF-8");
        assertTrue(text.contains("добавлен юнит U1"));
        assertTrue(text.contains("U1 танк (1,1)"));
        assertTrue(text.contains("убран"));
        assertTrue(text.contains("юниты:\r\nпока нет") || text.contains("юниты:\nпока нет"));
    }
}
