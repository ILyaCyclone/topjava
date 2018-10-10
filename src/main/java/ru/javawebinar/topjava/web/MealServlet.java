package ru.javawebinar.topjava.web;

import org.slf4j.Logger;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.model.MealWithExceed;
import ru.javawebinar.topjava.service.MealService;
import ru.javawebinar.topjava.service.ServiceFactory;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;

import static org.slf4j.LoggerFactory.getLogger;

public class MealServlet extends HttpServlet {
    private static final Logger log = getLogger(MealServlet.class);
    private static final int CALORIES_PER_DAY = 2000;

    private static final String ACTION_SAVE = "save"
            , ACTION_DELETE = "delete";

    private final MealService mealService;

    public MealServlet() {
        mealService = ServiceFactory.createMealsService();
    }

//    @Override
//    public void init() throws ServletException {
    // can't assign final in init()
//        mealService = ServiceFactory.createMealsService();
//    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        log.debug("serving meals");

        List<MealWithExceed> mealsWithExceed = mealService.getWithExceed(CALORIES_PER_DAY);

        log.trace("caloriesPerDay = {}", CALORIES_PER_DAY);
        log.trace("mealsWithExceed = {}", mealsWithExceed);

        request.setAttribute("mealsWithExceed", mealsWithExceed);
        request.setAttribute("caloriesPerDay", CALORIES_PER_DAY);

        log.debug("forward to /meals.jsp");
        request.getRequestDispatcher("/meals.jsp").forward(request, response);
    }


    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String action = request.getParameter("action");
        log.debug("MealServlet post action = {}", action);
        switch (action) {
            case ACTION_SAVE:
                Meal meal = createMealFromRequest(request);
                log.debug("save meal = {}", meal);
                mealService.save(meal);
                break;
            case ACTION_DELETE:
                Integer id = Integer.valueOf(request.getParameter("id"));
                log.debug("delete meal id = {}", id);
                mealService.deleteById(id);
                break;
        }

        log.debug("redirect to meals");
        response.sendRedirect("meals");
    }

    private Meal createMealFromRequest(HttpServletRequest request) {
        String idParam = request.getParameter("id");
        Integer id = idParam != null && idParam.trim().length() > 0 ? Integer.valueOf(idParam) : null;
        String datetimeParam = request.getParameter("datetime");
        LocalDateTime dateTime = LocalDateTime.parse(datetimeParam);
        String description = request.getParameter("description");
        log.debug("description = {}", description);
        Integer calories = Integer.valueOf(request.getParameter("calories"));

        return new Meal(id, dateTime, description, calories);
    }
}
