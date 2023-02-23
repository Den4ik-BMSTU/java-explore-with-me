package ru.practicum.stat.repositry;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ru.practicum.stat.model.Hit;
import ru.practicum.stat.model.ViewStats;

import java.time.LocalDateTime;
import java.util.List;

public interface HitRepositry extends JpaRepository<Hit, Long> {

    @Query(value = "SELECT new ru.practicum.stat.model.ViewStats(a.name, h.uri, COUNT(DISTINCT h.ip)) " +
            "FROM Hit h " +
            "LEFT JOIN App a ON h.appId = a.id " +
            "WHERE ((:uris) IS NULL OR h.uri IN (:uris)) AND h.created BETWEEN :start AND :end " +
            "GROUP BY h.appId, h.uri, a.name " +
            "ORDER BY COUNT(h.ip) DESC ")
    List<ViewStats> statByUniqueIp(@Param("start") LocalDateTime start,
                                   @Param("end") LocalDateTime end,
                                   @Param("uris") List<String> uris);

    @Query(value = "SELECT new ru.practicum.stat.model.ViewStats(a.name, h.uri, COUNT(h.ip)) " +
            "FROM Hit h " +
            "LEFT JOIN App a ON h.appId = a.id " +
            "WHERE h.uri IN (:uris) AND h.created BETWEEN :start AND :end " +
            "GROUP BY h.appId, h.uri, a.name " +
            "ORDER BY COUNT(h.ip) DESC ")
    List<ViewStats> statByIp(@Param("start") LocalDateTime start,
                                   @Param("end") LocalDateTime end,
                                   @Param("uris") List<String> uris);
}