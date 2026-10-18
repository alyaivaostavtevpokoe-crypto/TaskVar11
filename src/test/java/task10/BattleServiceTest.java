package task10;

import org.junit.jupiter.api.Test;
import ru.university.task10.repository.BattleRepository;
import ru.university.task10.repository.BattleRepositoryInMemoryImpl;
import ru.university.task10.service.BattleService;

import static org.junit.jupiter.api.Assertions.*;

public class BattleServiceTest {
    @Test
    public void addAndRemove() {
        BattleService s = new BattleService(new BattleRepositoryInMemoryImpl());
        int u = s.addUnit("танк", 2, 2, 4, 5, 1, 4, 2);
        assertEquals(1, u);
        assertEquals("U1", s.cell(3, 3));
        assertEquals(-1, s.addWall("камень", 3, 3, 1));
        assertEquals(-1, s.addUnit("край", 9, 9, 4, 5, 1, 2, 1));
        assertEquals(1, s.addWall("камень", 6, 6, 5));
        assertEquals("B1", s.cell(7, 7));
        assertTrue(s.removeUnit(u));
        assertEquals(".", s.cell(3, 3));
        assertTrue(s.removeWall(1));
        assertEquals(".", s.cell(7, 7));
    }

    @Test
    public void moveChecksAllSteps() {
        BattleRepository repo = new BattleRepositoryInMemoryImpl();
        BattleService s = new BattleService(repo);
        int u = s.addUnit("машина", 1, 1, 2, 5, 2, 3, 1);
        int w = s.addWall("стена", 4, 1, 1);
        assertFalse(s.move(u, 2, 2));
        assertEquals(1, repo.getUnit(u).getX());
        assertTrue(s.removeWall(w));
        assertTrue(s.move(u, 2, 2));
        assertEquals(3, repo.getUnit(u).getX());
        assertEquals("U1", s.cell(4, 1));
        assertFalse(s.move(u, 2, 3));
        int still = s.addUnit("пушка", 8, 8, 1, 5, 0, 3, 1);
        assertFalse(s.move(still, 4, 1));
    }

    @Test
    public void shotStopsAndDestroys() {
        BattleRepository repo = new BattleRepositoryInMemoryImpl();
        BattleService s = new BattleService(repo);
        int a = s.addUnit("А", 1, 1, 1, 5, 1, 5, 3);
        int b = s.addUnit("Б", 4, 1, 1, 5, 0, 2, 1);
        int w = s.addWall("щит", 3, 1, 1);
        assertTrue(s.shoot(a, 2).contains("препятствие"));
        assertEquals(5, repo.getUnit(b).getHp());
        s.removeWall(w);
        assertTrue(s.shoot(a, 2).contains("осталось здоровья: 2"));
        assertEquals(2, repo.getUnit(b).getHp());
        assertTrue(s.shoot(a, 2).contains("уничтожен"));
        assertNull(repo.getUnit(b));
        assertEquals(".", s.cell(4, 1));
    }

    @Test
    public void diagonalShotHasLimitedRange() {
        BattleRepository repo = new BattleRepositoryInMemoryImpl();
        BattleService s = new BattleService(repo);
        int a = s.addUnit("А", 1, 1, 1, 5, 1, 1, 2);
        int b = s.addUnit("Б", 3, 3, 1, 5, 0, 1, 1);
        assertEquals("никого не задело", s.shoot(a, 6));
        assertEquals(5, repo.getUnit(b).getHp());
        assertEquals(-1, s.addUnit("", 0, 0, 1, 5, 1, 1, 1));
    }
}
