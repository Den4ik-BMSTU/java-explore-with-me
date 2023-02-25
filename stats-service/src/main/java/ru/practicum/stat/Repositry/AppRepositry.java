package ru.practicum.stat.Repositry;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.stat.Model.App;

public interface AppRepositry extends JpaRepository<App, Long> {
    boolean existsByNameIgnoreCase(String name);

    App findAppByNameIgnoreCase(String name);
}
