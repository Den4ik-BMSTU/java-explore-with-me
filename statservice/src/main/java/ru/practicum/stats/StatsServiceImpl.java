package ru.practicum.stats;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.dto.EndpointHitDto;
import ru.practicum.mappers.StatsMapper;
import ru.practicum.model.ViewStats;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class StatsServiceImpl implements StatsService {
    private final StatsRepository repository;
    private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    @Override
    public List<ViewStats> getStats(String start, String end, List<String> uris, boolean unique) {
        List<ViewStats> viewStatsList = new ArrayList<>();
        for (String uri : uris) {
            String sqlQuery;
            if (unique) {
                sqlQuery = "SELECT app, uri, COUNT(DISTINCT ip) AS hits FROM endpoint_hit " +
                        "WHERE uri = :uri AND time_stamp > :start AND time_stamp < :end " +
                        "GROUP BY app, uri ";
            } else {
                sqlQuery = "SELECT app, uri, COUNT(ip) AS hits FROM endpoint_hit " +
                        "WHERE uri = :uri AND time_stamp > :start AND time_stamp < :end " +
                        "GROUP BY app, uri ";
            }
            SqlParameterSource namedParameters = new MapSqlParameterSource()
                    .addValue("uri", uri)
                    .addValue("start", StatsMapper.stringToLocalDateTime(start))
                    .addValue("end", StatsMapper.stringToLocalDateTime(end));
            List<ViewStats> viewStatsListByOneUri = namedParameterJdbcTemplate.query(sqlQuery, namedParameters, this::mapRow);
            viewStatsList.addAll(viewStatsListByOneUri);
        }
        viewStatsList.sort(Comparator.comparing(ViewStats::getHits).reversed());
        return viewStatsList;
    }

    @Transactional
    @Override
    public EndpointHitDto saveHit(EndpointHitDto endpointHitDto) {
        return StatsMapper.toEndpointHitDto(repository.save(StatsMapper.toEndpointHit(endpointHitDto)));
    }

    private ViewStats mapRow(ResultSet rs, int rowNum) throws SQLException {
        return ViewStats.builder()
                .app(rs.getString("app"))
                .uri(rs.getString("uri"))
                .hits(rs.getLong("hits"))
                .build();
    }
}