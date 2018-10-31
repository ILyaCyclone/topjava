package ru.javawebinar.topjava.model;

import org.hibernate.validator.constraints.Range;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

@NamedQueries({
        @NamedQuery(name = Meal.FIND_ALL, query = "SELECT m FROM Meal m WHERE m.user.id=:user_id ORDER BY m.dateTime DESC")
        , @NamedQuery(name = Meal.FIND_ALL_BETWEEN, query = "SELECT m FROM Meal m WHERE m.user.id=:user_id AND m.dateTime BETWEEN :start_date AND :end_date ORDER BY m.dateTime DESC")
        , @NamedQuery(name = Meal.DELETE, query = "DELETE FROM Meal m WHERE m.id=:id and m.user.id=:user_id")
        , @NamedQuery(name = Meal.UPDATE, query = "UPDATE Meal m SET m = :meal WHERE m.id=:id and m.user.id=:user_id")
})
@Entity
@Table(name = "meals", uniqueConstraints = {@UniqueConstraint(columnNames = {"user_id", "date_time"}, name = "meals_unique_user_datetime_idx")})
public class Meal extends AbstractBaseEntity {


    public static final String FIND_ALL = "Meal.findAll";
    public static final String FIND_ALL_BETWEEN = "Meal.findAllBetween";
    public static final String DELETE = "Meal.delete";
    public static final String UPDATE = "Meal.update";
    //    public static final String BY_EMAIL = "User.getByEmail";
//    public static final String ALL_SORTED = "User.getAllSorted";

    @Column(name = "date_time")
    @NotNull
    private LocalDateTime dateTime;

    @Column(name = "description")
    @NotBlank
    private String description;

    @Column(name = "calories")
    @Range(min = 1, message = "calories must be positive number")
    @NotNull
    private int calories;

    @ManyToOne(fetch = FetchType.LAZY)
    private User user;

    public Meal() {
    }

    public Meal(LocalDateTime dateTime, String description, int calories) {
        this(null, dateTime, description, calories);
    }

    public Meal(Integer id, LocalDateTime dateTime, String description, int calories) {
        super(id);
        this.dateTime = dateTime;
        this.description = description;
        this.calories = calories;
    }

    public LocalDateTime getDateTime() {
        return dateTime;
    }

    public String getDescription() {
        return description;
    }

    public int getCalories() {
        return calories;
    }

    public LocalDate getDate() {
        return dateTime.toLocalDate();
    }

    public LocalTime getTime() {
        return dateTime.toLocalTime();
    }

    public void setDateTime(LocalDateTime dateTime) {
        this.dateTime = dateTime;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setCalories(int calories) {
        this.calories = calories;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    @Override
    public String toString() {
        return "Meal{" +
                "id=" + id +
                ", dateTime=" + dateTime +
                ", description='" + description + '\'' +
                ", calories=" + calories +
                '}';
    }
}
