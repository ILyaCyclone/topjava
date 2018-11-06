package ru.javawebinar.topjava.repository.datajpa;

import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;
import ru.javawebinar.topjava.model.Meal;

import java.time.LocalDateTime;
import java.util.List;

public interface CrudMealRepository extends JpaRepository<Meal, Integer>, CrudMealRepositoryCustom {
    @Transactional
    @Modifying
    @Query("DELETE FROM Meal m WHERE m.id=:id AND m.user.id = :userId")
    int delete(@Param("id") int id, @Param("userId") int userId);

    @Query("SELECT m FROM Meal m WHERE m.user.id = :userId")
    List<Meal> findAllByUserId(@Param("userId") int userId, Sort sort);

    @Query("SELECT m FROM Meal m WHERE m.id=:id AND m.user.id = :userId")
    Meal findByIdAndUserId(@Param("id") int id, @Param("userId") int userId);

    @Query("SELECT m FROM Meal m WHERE m.dateTime BETWEEN :start_date AND :end_date AND m.user.id = :userId")
    List<Meal> findBetweenDates(@Param("start_date") LocalDateTime startDate, @Param("end_date") LocalDateTime endDate, @Param("userId") int userId, Sort sort);

//    @Transactional
//    Meal save(Meal meal, int userId);
}
