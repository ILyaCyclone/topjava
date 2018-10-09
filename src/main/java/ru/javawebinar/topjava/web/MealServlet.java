package ru.javawebinar.topjava.web;

import org.slf4j.Logger;
import ru.javawebinar.topjava.model.MealWithExceed;
import ru.javawebinar.topjava.service.MealService;
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

    private static final String ACTION_DELETE = "delete";

    private final MealService mealService;

    public MealServlet() {
        mealService = ServiceFactory.createMealsService();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        List<MealWithExceed> mealsWithExceed = mealService.getWithExceed(CALORIES_PER_DAY);
        request.setAttribute("mealsWithExceed", mealsWithExceed);
        request.setAttribute("caloriesPerDay", CALORIES_PER_DAY);

        log.debug("forward to meals");
        request.getRequestDispatcher("/meals.jsp").forward(request, response);
    }


    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String action = request.getParameter("action");
        switch (action) {
            case ACTION_DELETE:
                Integer id = Integer.valueOf(request.getParameter("id"));
                mealService.deleteById(id);
                break;
        }

        response.sendRedirect("meals");
    }
}
