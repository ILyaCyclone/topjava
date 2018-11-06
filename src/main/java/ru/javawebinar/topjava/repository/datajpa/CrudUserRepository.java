package ru.javawebinar.topjava.repository.datajpa;

import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;
import ru.javawebinar.topjava.model.User;

import java.util.List;
import java.util.Optional;

@Transactional(readOnly = true)
public interface CrudUserRepository extends JpaRepository<User, Integer> {
    @Transactional
    @Modifying
//    @Query(name = User.DELETE)
    @Query("DELETE FROM User u WHERE u.id=:id")
    int delete(@Param("id") int id);

    @Override
    @Transactional
    User save(User user);

    @Override
    @Transactional
    Optional<User> findById(Integer id);

    @Override
    @Transactional
    List<User> findAll(Sort sort);

    @Transactional
    User getByEmail(String email);

    @Transactional
    @Query("SELECT u FROM User u LEFT JOIN FETCH u.meals m WHERE u.id = :userId ORDER BY m.dateTime DESC")
    Optional<User> findByIdWithMeal(@Param("userId") int userId);
}
