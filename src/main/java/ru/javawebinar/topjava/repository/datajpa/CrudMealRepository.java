package ru.javawebinar.topjava.repository.datajpa;

import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.model.User;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface CrudMealRepository extends JpaRepository<Meal, Integer> {
    @Transactional
    int deleteByIdAndUser(int id, User user);

    @Transactional
    List<Meal> findAllByUser(User user, Sort sort);

    @Transactional
    Optional<Meal> findByIdAndUser(int id, User user);

    @Transactional
    List<Meal> findAllByUserAndDateTimeBetween(User user, LocalDateTime startDate, LocalDateTime endDate, Sort sort);

    @Transactional
    Meal save(Meal meal);

    @Query("SELECT m FROM Meal m INNER JOIN FETCH m.user WHERE m.id = ?1")
    Optional<Meal> findByIdWithUser(int id);
}
