package ru.job4j.dream.servlet;

import ru.job4j.dream.store.db.PhotoPsqlStore;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

/**
 * Class DownloadServlet.
 *
 * @author Vitaly Yagufarov (for.viy@gmail.com)
 * @version 1.0
 * @since 14.11.2020
 */
public class DownloadServlet extends HttpServlet {
    private static final PhotoPsqlStore PHOTO_STORE = PhotoPsqlStore.getStore();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setCharacterEncoding("UTF-8");
        resp.setContentType("text/html; charset=UTF-8");
        req.setAttribute("user", req.getSession().getAttribute("user"));
        int photoId = Integer.parseInt(req.getParameter("photoId"));
        String fileName = PHOTO_STORE.findById(photoId).getName();
        resp.setContentType("photoId=" + photoId);
        resp.setContentType("image/png; image/jpeg");
        resp.setHeader("Content-Disposition", "attachment; filename=\"" + fileName + "\"");
        File file = new File("images" + File.separator + "photo_id" + File.separator + fileName);
        try (FileInputStream in = new FileInputStream(file)) {
            resp.getOutputStream().write(in.readAllBytes());
        }
    }
}
