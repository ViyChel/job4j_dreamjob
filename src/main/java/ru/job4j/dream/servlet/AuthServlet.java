package ru.job4j.dream.servlet;

import ru.job4j.dream.model.User;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

/**
 * Class AuthServlet.
 *
 * @author Vitaly Yagufarov (for.viy@gmail.com)
 * @version 1.0
 * @since 23.11.2020
 */
public class AuthServlet extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setCharacterEncoding("UTF-8");
        resp.setContentType("text/html; charset=UTF-8");
        resp.setCharacterEncoding("UTF-8");
        String email = req.getParameter("email");
        String password = req.getParameter("password");
        HttpSession sc = req.getSession();
        User user = new User();
        if ("root@local".equals(email) && "root".equals(password)) {
            user.setName("Admin");
            user.setEmail(email);
            sc.setAttribute("user", user);
            resp.sendRedirect(req.getContextPath() + "/posts.do");
        } else if (!email.isEmpty() && !password.isEmpty()) {
            user.setName("User");
            user.setEmail(email);
            sc.setAttribute("user", user);
            resp.sendRedirect(req.getContextPath() + "/posts.do");
        } else {
            req.setAttribute("error", "Не верный email или пароль");
            req.getRequestDispatcher("login.jsp").forward(req, resp);
        }
    }
}
