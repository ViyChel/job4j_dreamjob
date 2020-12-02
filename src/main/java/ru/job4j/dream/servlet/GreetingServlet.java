package ru.job4j.dream.servlet;

import com.google.gson.JsonObject;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * Class GreetingServlet.
 *
 * @author Vitaly Yagufarov (for.viy@gmail.com)
 * @version 1.0
 * @since 02.12.2020
 */
public class GreetingServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("application/json");
        resp.setCharacterEncoding("UTF-8");
        String input = req.getParameter("text");
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("name", input);
        PrintWriter writer = resp.getWriter();
        writer.println(jsonObject);
        writer.flush();
    }
}
