package ru.javawebinar.topjava.web;

import org.slf4j.Logger;
import ru.javawebinar.topjava.model.MealWithExceed;
import ru.javawebinar.topjava.service.MealsService;
import ru.javawebinar.topjava.service.ServiceFactory;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

import static org.slf4j.LoggerFactory.getLogger;

public class MealServlet extends HttpServlet {
    private static final Logger log = getLogger(MealServlet.class);
    private static final int CALORIES_PER_DAY = 2000;

    private final MealsService mealsService;

    public MealServlet() {
        mealsService = ServiceFactory.createMealsService();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        List<MealWithExceed> mealsWithExceed = mealsService.getWithExceed(CALORIES_PER_DAY);
        request.setAttribute("mealsWithExceed", mealsWithExceed);
        request.setAttribute("caloriesPerDay", CALORIES_PER_DAY);

        log.debug("forward to meals");
        request.getRequestDispatcher("/meals.jsp").forward(request, response);
    }
}
