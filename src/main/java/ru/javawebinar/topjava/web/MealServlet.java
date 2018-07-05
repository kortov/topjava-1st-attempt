package ru.javawebinar.topjava.web;

import org.slf4j.Logger;
import ru.javawebinar.topjava.dao.DAO;
import ru.javawebinar.topjava.dao.impl.MealDAOImpl;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.model.MealWithExceed;
import ru.javawebinar.topjava.util.MealsUtil;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Comparator;
import java.util.List;

import static org.slf4j.LoggerFactory.getLogger;

public class MealServlet extends HttpServlet {
    private static final Logger log = getLogger(MealServlet.class);

    private static final String INSERT_OR_EDIT = "editmeal.jsp";
    private static final String LIST_MEALS = "meals.jsp";
    private static DAO<Meal> dao;

    @Override
    public void init() throws ServletException {
        super.init();
        dao = new MealDAOImpl();
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        log.debug("MealServlet doGet");
        String forwardUri = "";
        String action = request.getParameter("action");

        if (action.equalsIgnoreCase("delete")) {
            log.debug("MealServlet doGet, action delete");
            long mealId = Long.parseLong(request.getParameter("mealId"));
            dao.removeItem(mealId);
            forwardUri = LIST_MEALS;
            List<MealWithExceed> mealsWithExceed = getMealWithExceeds();
            request.setAttribute("meals", mealsWithExceed);
        } else if (action.equalsIgnoreCase("edit")) {
            log.debug("MealServlet doGet, action edit");
            forwardUri = INSERT_OR_EDIT;
            long mealId = Long.parseLong(request.getParameter("mealId"));
            Meal meal = dao.getItem(mealId);
            request.setAttribute("meal", meal);
        } else if (action.equalsIgnoreCase("list")) {
            log.debug("MealServlet doGet, action list");
            forwardUri = LIST_MEALS;
            List<MealWithExceed> mealsWithExceed = getMealWithExceeds();
            request.setAttribute("meals", mealsWithExceed);
        } else {
            forwardUri = INSERT_OR_EDIT;
        }

        RequestDispatcher view = request.getRequestDispatcher(forwardUri);
        view.forward(request, response);
    }

    private List<MealWithExceed> getMealWithExceeds() {
        List<Meal> meals = dao.getAll();
        List<MealWithExceed> filteredWithExceeded = MealsUtil
                .getFilteredWithExceeded(meals, LocalTime.MIN, LocalTime.MAX, 2000);
        filteredWithExceeded.sort(Comparator.comparing(MealWithExceed::getDateTime));
        return filteredWithExceeded;
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        log.debug("MealServlet doPost");
        request.setCharacterEncoding("UTF-8");
        String mealId = request.getParameter("mealId");
        String mealDateTime = request.getParameter("mealDateTime");
        LocalDateTime dateTime = LocalDateTime.parse(mealDateTime, DateTimeFormatter.ISO_DATE_TIME);
        String mealDescription = request.getParameter("mealDescription");
        int mealCalories = Integer.parseInt(request.getParameter("mealCalories"));
        if (mealId == null || mealId.isEmpty()) {
            Meal meal = new Meal(dateTime, mealDescription, mealCalories);
            dao.addItem(meal);
        } else {
            Meal meal = dao.getItem(Long.parseLong(mealId));
            log.debug("datetime:" + dateTime);
            Meal updatedMeal = new Meal(dateTime, mealDescription, mealCalories);
            dao.removeItem(meal.getId());
            dao.updateItem(updatedMeal);
        }
        RequestDispatcher view = request.getRequestDispatcher(LIST_MEALS);
        List<MealWithExceed> mealsWithExceed = getMealWithExceeds();
        request.setAttribute("meals", mealsWithExceed);
        view.forward(request, response);
    }
}
