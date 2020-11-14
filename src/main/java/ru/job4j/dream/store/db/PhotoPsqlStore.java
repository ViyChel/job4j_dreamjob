package ru.job4j.dream.store.db;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import ru.job4j.dream.model.Photo;
import ru.job4j.dream.store.Store;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Class PhotoPsqlStore.
 *
 * @author Vitaly Yagufarov (for.viy@gmail.com)
 * @version 1.0
 * @since 21.11.2020
 */
public class PhotoPsqlStore implements Store<Photo> {
    private static final PhotoPsqlStore STORE = new PhotoPsqlStore();
    private static final ConnectorDB CONNECTOR_DB = ConnectorDB.getInstance();
    private static final Log LOG = LogFactory.getLog(PhotoPsqlStore.class.getName());
    private static final String FIND_ALL = "SELECT * from photos;";
    private static final String FIND_BY_ID = "SELECT * FROM photos WHERE id = ?;";
    private static final String CREATE = "INSERT INTO photos (name) VALUES ( ? );";
    private static final String UPDATE = "UPDATE photos SET name = ?  WHERE id = ?;";
    private static final String DELETE = "DELETE FROM photos WHERE id = ?;";

    private PhotoPsqlStore() {
    }

    public static PhotoPsqlStore getStore() {
        return STORE;
    }

    private Connection connect() throws SQLException {
        return CONNECTOR_DB.getConnection();
    }

    @Override
    public Collection<Photo> findAll() {
        List<Photo> photos = new ArrayList<>();
        try (Connection cn = connect(); PreparedStatement ps = cn.prepareStatement(FIND_ALL)) {
            try (ResultSet it = ps.executeQuery()) {
                while (it.next()) {
                    photos.add(new Photo(it.getInt("id"),
                            it.getString("name")));
                }
            }
        } catch (Exception e) {
            LOG.error(e.getMessage(), e);
        }
        return photos;
    }

    @Override
    public Photo save(Photo model) {
        return model.getId() == 0 ? create(model) : update(model);
    }

    private Photo create(Photo model) {
        try (Connection cn = connect();
             PreparedStatement ps = cn.prepareStatement(CREATE, PreparedStatement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, model.getName());
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

    private Photo update(Photo model) {
        try (Connection cn = connect(); PreparedStatement ps = cn.prepareStatement(UPDATE)) {
            ps.setString(1, model.getName());
            ps.execute();
        } catch (Exception e) {
            LOG.error(e.getMessage(), e);
        }
        return model;
    }

    @Override
    public Photo findById(int id) {
        Photo photo = new Photo(0, "");
        try (Connection cn = connect(); PreparedStatement ps = cn.prepareStatement(FIND_BY_ID)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    photo.setId(rs.getInt("id"));
                    photo.setName(rs.getString("name"));
                }
            }
        } catch (Exception e) {
            LOG.error(e.getMessage(), e);
        }
        return photo;
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
