package ru.university.task10;

import org.junit.jupiter.api.Test;
import org.postgresql.ds.PGSimpleDataSource;
import ru.university.task10.repository.PlayerRepositoryDatabaseImpl;
import ru.university.task10.service.PlayerService;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assumptions.*;

public class PlayerDatabaseTest {
    @Test
    public void playerIsSavedInPostgres() throws Exception {
        String url = System.getenv("TEST_DB_URL");
        assumeTrue(url != null && !url.isEmpty());

        PGSimpleDataSource ds = new PGSimpleDataSource();
        ds.setUrl(url);
        ds.setUser(env("TEST_DB_USER", "postgres"));
        ds.setPassword(env("TEST_DB_PASSWORD", ""));
        PlayerRepositoryDatabaseImpl repo = new PlayerRepositoryDatabaseImpl(ds);
        repo.init();
        PlayerService s = new PlayerService(repo);
        String name = "игрок_" + System.nanoTime();
        int id = s.add(name);
        try {
            PlayerRepositoryDatabaseImpl another = new PlayerRepositoryDatabaseImpl(ds);
            assertEquals(name, another.getById(id).getName());
            assertTrue(s.rename(id, name + "_2"));
            assertEquals(name + "_2", another.getById(id).getName());
            assertEquals(-1, s.add(name.toUpperCase() + "_2"));
        } finally {
            s.delete(id);
        }
        assertNull(repo.getById(id));
    }

    private String env(String key, String defaultValue) {
        String value = System.getenv(key);
        return value == null ? defaultValue : value;
    }
}
