package ru.javawebinar.topjava.repository.jpa;

import org.springframework.stereotype.Repository;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.repository.MealRepository;
import ru.javawebinar.topjava.util.exception.NotFoundException;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.time.LocalDateTime;
import java.util.List;

@Repository
public class JpaMealRepositoryImpl implements MealRepository {

    @PersistenceContext
    private EntityManager em;

    @Override
    public Meal save(Meal meal, int userId) {
        if(meal.isNew()) {
            em.persist(meal);
            return meal;
        } else {
            Meal storedMeal = em.find(Meal.class, meal.getId());
            if(storedMeal.getUser().getId().equals(userId)) {
                return em.merge(meal);
            } else {
                throw new NotFoundException("meal id "+meal.getId()+" user_id "+userId+" not found");
            }
        }
    }

    @Override
    public boolean delete(int id, int userId) {
        int rowsAffected = em.createNamedQuery(Meal.DELETE_QUERY, Meal.class)
                .setParameter("id", id)
                .setParameter("user_id", userId)
                .executeUpdate();
        return rowsAffected > 0;
    }

    @Override
    public Meal get(int id, int userId) {
        return null;
    }

    @Override
    public List<Meal> getAll(int userId) {
        return null;
    }

    @Override
    public List<Meal> getBetween(LocalDateTime startDate, LocalDateTime endDate, int userId) {
        return null;
    }
}