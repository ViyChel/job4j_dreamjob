package ru.job4j.dream.store.db;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import ru.job4j.dream.model.Post;
import ru.job4j.dream.store.Store;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;


/**
 * Class PsqlStore.
 *
 * @author Vitaly Yagufarov (for.viy@gmail.com)
 * @version 1.0
 * @since 10.11.2020
 */
public class PostPsqlStore implements Store<Post> {
    private static final ConnectorDB CONNECTOR_DB = ConnectorDB.getInstance();
    private static final Log LOG = LogFactory.getLog(PostPsqlStore.class.getName());

    private PostPsqlStore() {
    }

    private static final class Lazy {
        private static final Store<Post> INST = new PostPsqlStore();
    }

    /**
     * Inst of store.
     *
     * @return the store
     */
    public static Store<Post> instOf() {
        return Lazy.INST;
    }

    private Connection connect() throws SQLException {
        return CONNECTOR_DB.getConnection();
    }

    @Override
    public Collection<Post> findAll() {
        List<Post> posts = new ArrayList<>();
        try (Connection cn = connect();
             PreparedStatement ps = cn.prepareStatement("SELECT * FROM posts;")
        ) {
            try (ResultSet it = ps.executeQuery()) {
                while (it.next()) {
                    posts.add(new Post(it.getInt("id"), it.getString("name"), it.getString("description")));
                }
            }
        } catch (Exception e) {
            LOG.error(e.getMessage(), e);
        }
        return posts;
    }

    @Override
    public Post findById(int id) {
        Post post = new Post(0, "", "");
        try (Connection cn = connect();
             PreparedStatement ps = cn.prepareStatement("SELECT * FROM posts WHERE id = ?;")
        ) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    post.setId(rs.getInt("id"));
                    post.setName(rs.getString("name"));
                    post.setDescription(rs.getString("description"));
                }
            }
        } catch (Exception e) {
            LOG.error(e.getMessage(), e);
        }
        return post;
    }

    @Override
    public void save(Post post) {
        if (post.getId() == 0) {
            create(post);
        } else {
            update(post);
        }
    }

    private Post create(Post post) {
        try (Connection cn = connect();
             PreparedStatement ps = cn.prepareStatement("INSERT INTO posts(name, description) VALUES (?, ?);",
                     PreparedStatement.RETURN_GENERATED_KEYS)
        ) {
            ps.setString(1, post.getName());
            ps.setString(2, post.getDescription());
            ps.execute();
            try (ResultSet id = ps.getGeneratedKeys()) {
                if (id.next()) {
                    post.setId(id.getInt(1));
                }
            }
        } catch (Exception e) {
            LOG.error(e.getMessage(), e);
        }
        return post;
    }

    private void update(Post post) {
        try (Connection cn = connect();
             PreparedStatement ps = cn.prepareStatement("UPDATE posts SET name =?, description =? WHERE id =?;")
        ) {
            ps.setString(1, post.getName());
            ps.setString(2, post.getDescription());
            ps.setInt(3, post.getId());
            ps.execute();
        } catch (Exception e) {
            LOG.error(e.getMessage(), e);
        }
    }
}