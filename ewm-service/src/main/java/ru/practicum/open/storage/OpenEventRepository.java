package ru.practicum.open.storage;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.stereotype.Repository;
import ru.practicum.comon.model.Event;

@Repository
public interface OpenEventRepository extends JpaRepository<Event, Long>, QuerydslPredicateExecutor<Event> {
}
