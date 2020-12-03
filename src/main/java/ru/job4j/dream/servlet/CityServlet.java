package ru.job4j.dream.servlet;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import ru.job4j.dream.model.City;
import ru.job4j.dream.store.Store;
import ru.job4j.dream.store.db.CityPsqlStore;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;
import java.util.Collection;

/**
 * Class CityServlet.
 *
 * @author Vitaly Yagufarov (for.viy@gmail.com)
 * @version 1.0
 * @since 04.12.2020
 */
public class CityServlet extends HttpServlet {
    private static final Store<City> STORE = CityPsqlStore.getStore();

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("application/json");
        resp.setCharacterEncoding("UTF-8");
        Collection<City> cities = STORE.findAll();
        JsonObject jsonObject = new JsonObject();
        PrintWriter writer = new PrintWriter(resp.getOutputStream(), true, StandardCharsets.UTF_8);
        cities.forEach(s -> jsonObject.addProperty(String.valueOf(s.getId()), s.getName()));
        writer.println(jsonObject);
        writer.flush();
    }
}
