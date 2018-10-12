package ru.javawebinar.topjava.repository.mock;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;
import ru.javawebinar.topjava.model.Role;
import ru.javawebinar.topjava.model.User;
import ru.javawebinar.topjava.repository.UserRepository;
import ru.javawebinar.topjava.util.exception.NotFoundException;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Predicate;
import java.util.stream.Collectors;

@Repository
public class InMemoryUserRepositoryImpl implements UserRepository {
    private Map<Integer, User> repository = new ConcurrentHashMap<>();
    private AtomicInteger counter = new AtomicInteger(0);

    public static void main(String[] args) {
        InMemoryUserRepositoryImpl repo = new InMemoryUserRepositoryImpl();
        System.out.println(repo.findByEmail("email2"));
        System.out.println(repo.findByEmail("emailZ"));
    }

    {
        Arrays.asList(
                new User("user1", "email1", "pass1", Role.ROLE_USER),
                new User("user2", "email2", "pass2", Role.ROLE_USER),
                new User("user3", "email3", "pass3", Role.ROLE_USER),
                new User("admin1", "aemail1", "apass1", Role.ROLE_ADMIN, Role.ROLE_USER)
        ).forEach(this::save);

    }

    private static final Logger log = LoggerFactory.getLogger(InMemoryUserRepositoryImpl.class);

    @Override
    public boolean delete(int id) {
        log.info("delete {}", id);
        return repository.remove(id) != null;
    }

    @Override
    public User save(User user) {
        if (user.isNew()) {
            user.setId(counter.incrementAndGet());
            repository.put(user.getId(), user);
            return user;
        }
        // treat case: update, but absent in storage
        return repository.computeIfPresent(user.getId(), (id, oldUser) -> user);
    }

    @Override
    public User findOne(int id) {
        log.debug("findOne {}", id);
        return repository.get(id);
    }

    private Collection<User> getAllRepositoryValues() {
        return repository.values();
    }

    @Override
    public List<User> findAll() {
        log.debug("findAll");
        return getAllRepositoryValues().stream()
                .sorted()
                .collect(Collectors.toList());
    }

    @Override
    public User findByEmail(String email) {
        log.debug("findByEmail {}", email);

        return findByPredicate(user -> user.getEmail().equalsIgnoreCase(email));
    }

    private User findByPredicate(Predicate<User> predicate) {
        return getAllRepositoryValues().stream()
                .filter(predicate)
                .findAny()
                .orElse(null);
    }
}
