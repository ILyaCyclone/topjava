package ru.javawebinar.topjava.service.jdbc;

import org.springframework.test.context.ActiveProfiles;
import ru.javawebinar.topjava.Profiles;
import ru.javawebinar.topjava.service.AbstractUserServiceTest;


@ActiveProfiles({Profiles.ACTIVE_DB, Profiles.JDBC})
public class JdbcUserServiceTest extends AbstractUserServiceTest {

}