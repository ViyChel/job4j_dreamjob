package ru.job4j.dream.servlet;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import ru.job4j.dream.model.Post;
import ru.job4j.dream.store.Store;
import ru.job4j.dream.store.db.PostPsqlStore;
import ru.job4j.dream.store.db.PostPsqlStoreStub;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * Class PostServletTest.
 *
 * @author Vitaly Yagufarov (for.viy@gmail.com)
 * @version 1.0
 * @since 30.11.2020
 */
@RunWith(PowerMockRunner.class)
@PrepareForTest(PostPsqlStore.class)
public class PostServletTest {

    @Test
    public void whenAddPostThenStoreIt() throws ServletException, IOException {
        Store<Post> store = PostPsqlStoreStub.getINST();
        PowerMockito.mockStatic(PostPsqlStore.class);
        Mockito.when(PostPsqlStore.instOf()).thenReturn(store);
        HttpServletRequest req = mock(HttpServletRequest.class);
        HttpServletResponse resp = mock(HttpServletResponse.class);
        when(req.getParameter("id")).thenReturn("0");
        when(req.getParameter("name")).thenReturn("Java junior");
        when(req.getParameter("description")).thenReturn("first level");
        new PostServlet().doPost(req, resp);
        assertThat(store.findAll().iterator().next().getName(), is("Java junior"));
    }
}