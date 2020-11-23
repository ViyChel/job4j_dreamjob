package ru.job4j.dream.servlet;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import ru.job4j.dream.model.Candidate;
import ru.job4j.dream.model.Photo;
import ru.job4j.dream.store.Store;
import ru.job4j.dream.store.db.CandidatePsqlStore;
import ru.job4j.dream.store.db.PhotoPsqlStore;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.UUID;

/**
 * Class CandidateServlet.
 *
 * @author Vitaly Yagufarov (for.viy@gmail.com)
 * @version 1.0
 * @since 09.11.2020
 */
public class CandidateServlet extends HttpServlet {
    private static final Store<Candidate> STORE = CandidatePsqlStore.instOf();
    private static final PhotoPsqlStore PHOTO_STORE = PhotoPsqlStore.getStore();
    private static final Log LOG = LogFactory.getLog(CandidatePsqlStore.class.getName());

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setCharacterEncoding("UTF-8");
        resp.setContentType("text/html; charset=UTF-8");
        if (req.getParameter("action") != null) {
            if ("delete".equals(req.getParameter("action"))) {
                STORE.delete(Integer.parseInt(req.getParameter("id")));
            }
        }
        req.setAttribute("candidates", STORE.findAll());
        req.setAttribute("user", req.getSession().getAttribute("user"));
        req.getRequestDispatcher("/candidate/candidates.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setCharacterEncoding("UTF-8");
        resp.setContentType("text/html; charset=UTF-8");
        DiskFileItemFactory factory = new DiskFileItemFactory();
        ServletContext servletContext = this.getServletConfig().getServletContext();
        File repository = (File) servletContext.getAttribute("javax.servlet.context.tempdir");
        factory.setRepository(repository);
        ServletFileUpload upload = new ServletFileUpload(factory);
        Candidate candidate = new Candidate();
        try {
            List<FileItem> items = upload.parseRequest(req);
            File folder = new File("images" + File.separator +"photo_id");
            if (!folder.exists()) {
                folder.mkdirs();
            }
            for (FileItem item : items) {
                if (!item.isFormField()) {
                    if (!item.getName().isEmpty()) {
                        String name = UUID.randomUUID() + item.getName();
                        String path = folder + File.separator + name;
                        File file = new File(path);
                        try (FileOutputStream out = new FileOutputStream(file)) {
                            out.write(item.getInputStream().readAllBytes());
                        }
                        Photo photo = PHOTO_STORE.save(new Photo(0, name));
                        candidate.setPhoto(photo);
                    }
                } else {
                    String name = item.getFieldName();
                    if ("name".equals(name)) {
                        candidate.setName(item.getString("UTF-8"));
                    }
                }
            }
        } catch (FileUploadException e) {
            LOG.error(e.getMessage(), e);
        }
        if (req.getParameter("id") != null) {
            candidate.setId(Integer.parseInt(req.getParameter("id")));
        }
        STORE.save(candidate);
        req.setAttribute("user", req.getSession().getAttribute("user"));
        resp.sendRedirect(req.getContextPath() + "/candidates.do");
    }
}

