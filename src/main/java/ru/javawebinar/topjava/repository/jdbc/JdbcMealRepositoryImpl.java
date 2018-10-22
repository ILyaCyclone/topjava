package ru.javawebinar.topjava.repository.jdbc;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.support.DataAccessUtils;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.repository.MealRepository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public class JdbcMealRepositoryImpl implements MealRepository {

    private static final BeanPropertyRowMapper<Meal> ROW_MAPPER = BeanPropertyRowMapper.newInstance(Meal.class);

    private final JdbcTemplate jdbcTemplate;

    private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    private final SimpleJdbcInsert insert;

    @Autowired
    public JdbcMealRepositoryImpl(JdbcTemplate jdbcTemplate, NamedParameterJdbcTemplate namedParameterJdbcTemplate) {
        this.insert = new SimpleJdbcInsert(jdbcTemplate)
                .withTableName("meals")
                .usingGeneratedKeyColumns("id");
        this.jdbcTemplate = jdbcTemplate;
        this.namedParameterJdbcTemplate = namedParameterJdbcTemplate;
    }
    @Override
    public Meal save(Meal meal, int userId) {
        MapSqlParameterSource map = new MapSqlParameterSource()
                .addValue("id", meal.getId())
                .addValue("userId", userId)
                .addValue("dateTime", meal.getDateTime())
                .addValue("description", meal.getDescription())
                .addValue("calories", meal.getCalories());
        if(meal.isNew()) {
            Number key = insert.executeAndReturnKey(map);
            meal.setId(key.intValue());
        } else if (namedParameterJdbcTemplate.update(
                "UPDATE meals SET date_time=:dateTime, description=:description, calories=:calories " +
                        "WHERE id=:id and user_id = :userId", map) == 0) {
            return null;
        }
        return meal;
    }

    @Override
    public boolean delete(int id, int userId) {
        return namedParameterJdbcTemplate.update("DELETE meals WHERE id=:id and user_id = :user_id"
            , new MapSqlParameterSource("id", id)
                        .addValue("user_id", userId)
        ) != 0;
    }

    @Override
    public Meal get(int id, int userId) {
        return DataAccessUtils.singleResult(namedParameterJdbcTemplate.query("SELECT * FROM meals WHERE id=:id and user_id = :user_id"
            , new MapSqlParameterSource("id", id)
                        .addValue("user_id", userId)
            , ROW_MAPPER));
    }

    @Override
    public List<Meal> getAll(int userId) {
        return jdbcTemplate.query("SELECT * FROM meals WHERE user_id=? ORDER BY date_time desc"
                , ROW_MAPPER, userId);
    }

    @Override
    public List<Meal> getBetween(LocalDateTime startDate, LocalDateTime endDate, int userId) {
        return namedParameterJdbcTemplate.query("SELECT * FROM meals WHERE user_id=:user_id " +
                        "AND date_time >= :start_date AND date_time <= :end_date " +
                        "ORDER BY date_time desc"
                , new MapSqlParameterSource("user_id", userId)
                    .addValue("start_date", startDate)
                    .addValue("end_date", endDate)
                , ROW_MAPPER);
    }
}
