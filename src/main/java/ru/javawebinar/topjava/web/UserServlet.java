package ru.javawebinar.topjava.web;

import org.slf4j.*;
import org.springframework.web.context.*;
import org.springframework.web.context.support.*;
import ru.javawebinar.topjava.web.user.*;

import javax.servlet.*;
import javax.servlet.http.*;
import java.io.*;

import static org.slf4j.LoggerFactory.*;

public class UserServlet extends HttpServlet {
    private static final Logger log = getLogger(UserServlet.class);

    private AdminRestController adminController;

    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);
        WebApplicationContext springContext = WebApplicationContextUtils.getRequiredWebApplicationContext(getServletContext());
        adminController = springContext.getBean(AdminRestController.class);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        int userId = Integer.parseInt(request.getParameter("userId"));
        SecurityUtil.setAuthUserId(userId);
        response.sendRedirect("meals");
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        log.debug("getAll");
        request.setAttribute("users", adminController.getAll());
        request.getRequestDispatcher("/users.jsp").forward(request, response);
    }
}