package ru.practicum.stat.repositry;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.stat.model.App;

public interface AppRepositry extends JpaRepository<App, Long> {
    boolean existsByNameIgnoreCase(String name);

    App findAppByNameIgnoreCase(String name);
}
