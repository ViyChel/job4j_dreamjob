package ru.job4j.dream.store.db;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import ru.job4j.dream.model.User;
import ru.job4j.dream.store.Store;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Class UserPsqlStore.
 *
 * @author Vitaly Yagufarov (for.viy@gmail.com)
 * @version 1.0
 * @since 24.11.2020
 */
public class UserPsqlStore implements Store<User> {
    private static final UserPsqlStore STORE = new UserPsqlStore();
    private static final ConnectorDB CONNECTOR_DB = ConnectorDB.getInstance();
    private static final Log LOG = LogFactory.getLog(UserPsqlStore.class.getName());
    private static final String FIND_ALL = "SELECT * from users;";
    private static final String FIND_BY_ID = "SELECT * FROM users WHERE id = ?;";
    private static final String CREATE = "INSERT INTO users (name, email, password) VALUES (?, ?, ?);";
    private static final String UPDATE = "UPDATE users SET name = ?, email = ?, password = ?  WHERE id = ?;";
    private static final String DELETE = "DELETE FROM users WHERE id = ?;";

    private UserPsqlStore() {
    }

    public static UserPsqlStore getStore() {
        return STORE;
    }

    private Connection connect() throws SQLException {
        return CONNECTOR_DB.getConnection();
    }


    @Override
    public Collection<User> findAll() {
        List<User> users = new ArrayList<>();
        try (Connection cn = connect(); PreparedStatement ps = cn.prepareStatement(FIND_ALL)) {
            try (ResultSet it = ps.executeQuery()) {
                while (it.next()) {
                    users.add(new User(it.getInt("id"),
                            it.getString("name"),
                            it.getString("email"),
                            it.getString("password")));
                }
            }
        } catch (Exception e) {
            LOG.error(e.getMessage(), e);
        }
        return users;
    }

    @Override
    public User save(User model) {
        return model.getId() == 0 ? create(model) : update(model);
    }

    private User create(User model) {
        try (Connection cn = connect();
             PreparedStatement ps = cn.prepareStatement(CREATE, PreparedStatement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, model.getName());
            ps.setString(2, model.getEmail());
            ps.setString(3, model.getPassword());
            ps.execute();
            try (ResultSet id = ps.getGeneratedKeys()) {
                if (id.next()) {
                    model.setId(id.getInt(1));
                }
            }
        } catch (Exception e) {
            LOG.error(e.getMessage(), e);
        }
        return model;
    }

    private User update(User model) {
        try (Connection cn = connect(); PreparedStatement ps = cn.prepareStatement(UPDATE)) {
            ps.setString(1, model.getName());
            ps.setString(2, model.getEmail());
            ps.setString(3, model.getPassword());
            ps.setInt(4, model.getId());
            ps.execute();
        } catch (Exception e) {
            LOG.error(e.getMessage(), e);
        }
        return model;
    }

    @Override
    public User findById(int id) {
        User user = new User();
        try (Connection cn = connect(); PreparedStatement ps = cn.prepareStatement(FIND_BY_ID)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    user.setId(rs.getInt("id"));
                    user.setName(rs.getString("name"));
                    user.setEmail(rs.getString("email"));
                    user.setPassword(rs.getString("password"));
                }
            }
        } catch (Exception e) {
            LOG.error(e.getMessage(), e);
        }
        return user;
    }

    @Override
    public boolean delete(int id) {
        boolean result = false;
        try (Connection cn = connect(); PreparedStatement ps = cn.prepareStatement(DELETE)) {
            ps.setInt(1, id);
            result = ps.executeUpdate() > 0;
        } catch (Exception e) {
            LOG.error(e.getMessage(), e);
        }
        return result;
    }
}
