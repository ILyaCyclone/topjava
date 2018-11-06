package ru.javawebinar.topjava.repository.datajpa;

import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;
import ru.javawebinar.topjava.model.Meal;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Transactional(readOnly = true)
public interface CrudMealRepository extends JpaRepository<Meal, Integer> {
    @Transactional
    int deleteByIdAndUserId(int id, int userId);

    List<Meal> findAllByUserId(int userId, Sort sort);

    Optional<Meal> findByIdAndUserId(int id, int userId);

    List<Meal> findAllByUserIdAndDateTimeBetween(int userId, LocalDateTime startDate, LocalDateTime endDate, Sort sort);

    @Transactional
    Meal save(Meal meal);

    @Query("SELECT m FROM Meal m INNER JOIN FETCH m.user WHERE m.id = ?1")
    Optional<Meal> findByIdWithUser(int id);
}
