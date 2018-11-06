package ru.javawebinar.topjava.repository.jdbc;

import org.springframework.context.annotation.Profile;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;
import ru.javawebinar.topjava.Profiles;

import java.sql.Timestamp;
import java.time.LocalDateTime;

/**
 * HSQLDB can't work with Java 8 LocalDateTime,
 * so convert it to Timestamp
 */
@Repository
@Profile(Profiles.HSQL_DB)
public class HsqlJdbcMealRepository extends AbstractJdbcMealRepository {
    public HsqlJdbcMealRepository(JdbcTemplate jdbcTemplate, NamedParameterJdbcTemplate namedParameterJdbcTemplate) {
        super(jdbcTemplate, namedParameterJdbcTemplate);
    }

    @Override
    Object convertDateToDBSpecific(LocalDateTime date) {
        return Timestamp.valueOf(date);
    }
}
