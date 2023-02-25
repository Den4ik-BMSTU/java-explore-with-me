package ru.practicum.admyn.storage;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.practicum.comon.model.User;

import java.util.List;

@Repository
public interface AdmUserRepository extends JpaRepository<User, Long> {

    List<User> findAllByIdIn(List<Long> ids, Pageable pageable);

    boolean existsByEmail(String email);

}
