package ru.javawebinar.topjava.repository.jdbc;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.support.DataAccessUtils;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import ru.javawebinar.topjava.model.Role;
import ru.javawebinar.topjava.model.User;
import ru.javawebinar.topjava.repository.UserRepository;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Repository
@Transactional(readOnly = true)

public class JdbcUserRepositoryImpl implements UserRepository {

    private static final BeanPropertyRowMapper<User> ROW_MAPPER = BeanPropertyRowMapper.newInstance(User.class);

    private final JdbcTemplate jdbcTemplate;

    private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    private final SimpleJdbcInsert insertUser;

    @Autowired
    public JdbcUserRepositoryImpl(JdbcTemplate jdbcTemplate, NamedParameterJdbcTemplate namedParameterJdbcTemplate) {
        this.insertUser = new SimpleJdbcInsert(jdbcTemplate)
                .withTableName("users")
                .usingGeneratedKeyColumns("id");

        this.jdbcTemplate = jdbcTemplate;
        this.namedParameterJdbcTemplate = namedParameterJdbcTemplate;
    }

    @Override
    @Transactional
    public User save(User user) {
        BeanPropertySqlParameterSource parameterSource = new BeanPropertySqlParameterSource(user);

        if (user.isNew()) {
            Number newKey = insertUser.executeAndReturnKey(parameterSource);
            user.setId(newKey.intValue());
            insertRoles(user.getId(), user.getRoles());
        } else {
            int updated = namedParameterJdbcTemplate.update(
                    "UPDATE users SET name=:name, email=:email, password=:password, " +
                            "registered=:registered, enabled=:enabled, calories_per_day=:caloriesPerDay WHERE id=:id", parameterSource);
            if (updated == 0) {
                return null;
            }
            updateRoles(user);
        }
        return user;
    }


    @Override
    @Transactional
    public boolean delete(int id) {
        return jdbcTemplate.update("DELETE FROM users WHERE id=?", id) != 0;
    }

    @Override
    public User get(int id) {
        List<User> users = jdbcTemplate.query("SELECT * FROM users WHERE id=?", ROW_MAPPER, id);
        User user = DataAccessUtils.singleResult(users);
        if (user != null) {
            assignRoles(user);
        }
        return user;
    }

    @Override
    public User getByEmail(String email) {
//        return jdbcTemplate.queryForObject("SELECT * FROM users WHERE email=?", ROW_MAPPER, email);
        List<User> users = jdbcTemplate.query("SELECT * FROM users WHERE email=?", ROW_MAPPER, email);
        User user = DataAccessUtils.singleResult(users);
        if (user != null) {
            assignRoles(user);
        }
        return user;
    }

    @Override
    public List<User> getAll() {
        List<User> users = jdbcTemplate.query("SELECT * FROM users ORDER BY name, email", ROW_MAPPER);
        assignRoles(users);
        return users;
    }



    private void insertRoles(int userId, Collection<Role> roles) {
        String sql = "INSERT INTO user_roles(user_id, role) values (?, ?)";

        jdbcTemplate.batchUpdate(sql, new BatchPreparedStatementSetter() {
            @Override
            public void setValues(PreparedStatement ps, int i) throws SQLException {
                ps.setInt(1, userId);
                ps.setString(2, roles.toArray()[i].toString());
            }

            @Override
            public int getBatchSize() {
                return roles.size();
            }

        });
    }


    private void updateRoles(User user) {
        jdbcTemplate.update("DELETE FROM user_roles WHERE user_id = ?", user.getId());
        insertRoles(user.getId(), user.getRoles());
    }


    private void assignRoles(final User user) {
        assignRoles(Collections.singletonList(user));
    }

    private void assignRoles(final List<User> users) {
        if (users.isEmpty()) return;

        class UserRole {
            private int userId;
            private Role role;

            public UserRole(int userId, Role role) {
                this.userId = userId;
                this.role = role;
            }

            public int getUserId() {
                return userId;
            }

            public Role getRole() {
                return role;
            }
        }

        // collect users IDs
        List<Integer> ids = users.stream()
                .map(User::getId)
                .collect(Collectors.toList());

        // query user_roles
        NamedParameterJdbcTemplate namedParameterJdbcTemplate = new NamedParameterJdbcTemplate(jdbcTemplate);
        List<UserRole> userRoles = namedParameterJdbcTemplate.query("SELECT * FROM user_roles WHERE user_id in (:ids)"
                , new MapSqlParameterSource("ids", ids)
                , ((rs, i) -> new UserRole(rs.getInt("USER_ID"), Role.valueOf(rs.getString("ROLE")))));

        // assign roles to users
        users.forEach(u -> u.setRoles(
                userRoles.stream()
                        .filter(ur -> ur.getUserId() == u.getId())
                        .map(UserRole::getRole)
                        .collect(Collectors.toList())
        ));

    }

}
