package ru.job4j.dream.servlet;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import ru.job4j.dream.model.User;
import ru.job4j.dream.store.Store;
import ru.job4j.dream.store.db.CandidatePsqlStore;
import ru.job4j.dream.store.db.UserPsqlStore;

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
    private static final Store<User> STORE = UserPsqlStore.getStore();
    private static final Log LOG = LogFactory.getLog(CandidatePsqlStore.class.getName());

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setCharacterEncoding("UTF-8");
        resp.setContentType("text/html; charset=UTF-8");
        if ("true".equals(req.getParameter("exit"))) {
            req.getSession().setAttribute("user", null);
        }
        req.setAttribute("error", null);
        req.getRequestDispatcher("/auth/login.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setCharacterEncoding("UTF-8");
        resp.setContentType("text/html; charset=UTF-8");
        String email = req.getParameter("email");
        String password = req.getParameter("password");
        HttpSession sc = req.getSession();
        if (!"".equals(email) && !"".equals(password)) {
            User user = ((UserPsqlStore) STORE).findByEmail(email);
            if (user.getId() != 0 && user.getPassword().equals(password)) {
                sc.setAttribute("user", user);
                resp.sendRedirect(req.getContextPath() + "/posts.do");
            } else {
                authFail(req, resp);
            }
        } else {
            authFail(req, resp);
        }
    }

    private void authFail(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setAttribute("error", "Не верный email или пароль");
        req.getRequestDispatcher("/auth/login.jsp").forward(req, resp);
    }
}
