package ru.practicum.ewm.User.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.ewm.User.model.User;

import java.util.Collection;
import java.util.List;

public interface UserRepository extends JpaRepository<User, Long> {
    List<User> findUserByIdIn(List<Integer> ids);

    List<User> findByIdIn(Collection<Long> id);
}
