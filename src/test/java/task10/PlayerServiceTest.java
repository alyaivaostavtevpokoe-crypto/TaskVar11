package ru.university.task10;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.Scanner;
import org.junit.jupiter.api.Test;
import ru.university.task10.controller.CmdController;
import ru.university.task10.repository.BattleRepositoryInMemoryImpl;
import ru.university.task10.repository.PlayerRepositoryInMemoryImpl;
import ru.university.task10.service.BattleService;
import ru.university.task10.service.PlayerService;

import static org.junit.jupiter.api.Assertions.*;

public class PlayerServiceTest {
    @Test
    public void playersCanBeAddedRenamedAndRemoved() throws Exception {
        PlayerService s = new PlayerService(new PlayerRepositoryInMemoryImpl());
        assertEquals(-1, s.add(" "));
        assertEquals(1, s.add("  Аня  "));
        assertEquals("Аня", s.getById(1).getName());
        assertEquals(-1, s.add("аня"));
        assertEquals(2, s.add("Маша"));
        assertFalse(s.rename(2, "АНЯ"));
        assertTrue(s.rename(2, "Катя"));
        assertEquals("Катя", s.getById(2).getName());
        assertTrue(s.delete(1));
        assertNull(s.getById(1));
        assertEquals(1, s.getAll().size());
    }

    @Test
    public void playerMenuWorks() throws Exception {
        String input = "9\n2\nАня\n1\n3\n1\nМаша\n1\n4\n1\n1\n0\n0\n";
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        PrintStream old = System.out;
        try {
            System.setOut(new PrintStream(bytes, true, "UTF-8"));
            new CmdController(new BattleService(new BattleRepositoryInMemoryImpl()),
                    new PlayerService(new PlayerRepositoryInMemoryImpl()), new Scanner(input)).run();
        } finally {
            System.setOut(old);
        }
        String text = bytes.toString("UTF-8");
        assertTrue(text.contains("добавлен игрок 1"));
        assertTrue(text.contains("1 - Аня"));
        assertTrue(text.contains("переименован"));
        assertTrue(text.contains("1 - Маша"));
        assertTrue(text.contains("удален"));
        assertTrue(text.contains("игроков пока нет"));
    }
}
