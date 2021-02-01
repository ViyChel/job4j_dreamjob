package ru.job4j.dream.store.db;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import ru.job4j.dream.model.City;
import ru.job4j.dream.store.Store;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Class CityPsqlStore.
 *
 * @author Vitaly Yagufarov (for.viy@gmail.com)
 * @version 1.0
 * @since 03.12.2020
 */
public class CityPsqlStore implements Store<City> {
    private static final CityPsqlStore STORE = new CityPsqlStore();
    private static final ConnectorDB CONNECTOR_DB = ConnectorDB.getInstance();
    private static final Log LOG = LogFactory.getLog(CityPsqlStore.class.getName());
    private static final String FIND_ALL = "SELECT * from cities;";
    private static final String FIND_BY_ID = "SELECT * FROM cities WHERE id = ?;";
    private static final String CREATE = "INSERT INTO cities (name) VALUES ( ? );";
    private static final String UPDATE = "UPDATE cities SET name = ?  WHERE id = ?;";
    private static final String DELETE = "DELETE FROM cities WHERE id = ?;";

    private CityPsqlStore() {
    }

    public static CityPsqlStore getStore() {
        return STORE;
    }

    private Connection connect() throws SQLException {
        return CONNECTOR_DB.getConnection();
    }

    @Override
    public Collection<City> findAll() {
        List<City> cities = new ArrayList<>();
        try (Connection cn = connect(); PreparedStatement ps = cn.prepareStatement(FIND_ALL)) {
            try (ResultSet it = ps.executeQuery()) {
                while (it.next()) {
                    cities.add(new City(it.getInt("id"),
                            it.getString("name")));
                }
            }
        } catch (Exception e) {
            LOG.error(e.getMessage(), e);
        }
        return cities;
    }

    @Override
    public City save(City model) {
        return model.getId() == 0 ? create(model) : update(model);
    }

    private City create(City model) {
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

    private City update(City model) {
        try (Connection cn = connect(); PreparedStatement ps = cn.prepareStatement(UPDATE)) {
            ps.setString(1, model.getName());
            ps.setInt(2, model.getId());
            ps.execute();
        } catch (Exception e) {
            LOG.error(e.getMessage(), e);
        }
        return model;
    }

    @Override
    public City findById(int id) {
        City city = new City(0, "");
        try (Connection cn = connect(); PreparedStatement ps = cn.prepareStatement(FIND_BY_ID)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    city.setId(rs.getInt("id"));
                    city.setName(rs.getString("name"));
                }
            }
        } catch (Exception e) {
            LOG.error(e.getMessage(), e);
        }
        return city;
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