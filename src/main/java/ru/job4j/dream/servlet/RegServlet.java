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
 * Class RegServlet.
 *
 * @author Vitaly Yagufarov (for.viy@gmail.com)
 * @version 1.0
 * @since 24.11.2020
 */
public class RegServlet extends HttpServlet {
    private static final Store<User> STORE = UserPsqlStore.getStore();
    private static final Log LOG = LogFactory.getLog(CandidatePsqlStore.class.getName());

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setCharacterEncoding("UTF-8");
        resp.setContentType("text/html; charset=UTF-8");
        req.setAttribute("user", req.getSession().getAttribute("user"));
        req.getRequestDispatcher("/auth/reg.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setCharacterEncoding("UTF-8");
        resp.setContentType("text/html; charset=UTF-8");
        String name = req.getParameter("name");
        String email = req.getParameter("email");
        String password = req.getParameter("password");
        HttpSession sc = req.getSession();
        if (!name.isEmpty() && !email.isEmpty() && !password.isEmpty()) {
            User user = ((UserPsqlStore) STORE).findByEmail(email);
            if (user.getId() != 0) {
                req.setAttribute("error", "Пользователь с таким email уже существует!");
                doGet(req, resp);
            } else {
                user.setName(name);
                user.setEmail(email);
                user.setPassword(password);
                STORE.save(user);
                sc.setAttribute("user", user);
                resp.sendRedirect(req.getContextPath() + "/posts.do");
            }
        } else {
            req.setAttribute("error", "Заполните все поля!");
            doGet(req, resp);
        }
    }
}
