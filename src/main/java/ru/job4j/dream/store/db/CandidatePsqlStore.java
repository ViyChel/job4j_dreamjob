package ru.job4j.dream.store.db;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import ru.job4j.dream.model.Candidate;
import ru.job4j.dream.model.Photo;
import ru.job4j.dream.store.Store;

import java.sql.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Class CandidatePsqlStore.
 *
 * @author Vitaly Yagufarov (for.viy@gmail.com)
 * @version 1.0
 * @since 12.11.2020
 */
public class CandidatePsqlStore implements Store<Candidate> {
    private static final ConnectorDB CONNECTOR_DB = ConnectorDB.getInstance();
    private static final Store<Photo> PHOTO_STORE = PhotoPsqlStore.getStore();
    private static final Log LOG = LogFactory.getLog(CandidatePsqlStore.class.getName());
    private static final String FIND_ALL = "SELECT * FROM candidates;";
    private static final String FIND_BY_ID = "SELECT * FROM candidates WHERE id = ?;";
    private static final String CREATE = "INSERT INTO candidates (name, photo_id) VALUES (? , ?);";
    private static final String UPDATE = "UPDATE candidates SET name = ?, photo_id = ?  WHERE id = ?;";
    private static final String DELETE = "DELETE FROM candidates WHERE id = ?;";


    private CandidatePsqlStore() {
    }

    private static final class Lazy {
        private static final Store<Candidate> INST = new CandidatePsqlStore();
    }

    /**
     * Inst of store.
     *
     * @return the store
     */
    public static Store<Candidate> instOf() {
        return Lazy.INST;
    }

    private Connection connect() throws SQLException {
        return CONNECTOR_DB.getConnection();
    }

    @Override
    public Collection<Candidate> findAll() {
        List<Candidate> candidates = new ArrayList<>();
        try (Connection cn = connect(); PreparedStatement ps = cn.prepareStatement(FIND_ALL)) {
            try (ResultSet it = ps.executeQuery()) {
                while (it.next()) {
                    Candidate candidate = new Candidate(it.getInt("id"),
                            it.getString("name"));
                    Integer photoId = it.getObject("photo_id", Integer.class);
                    if (photoId != null) {
                        candidate.setPhoto(PHOTO_STORE.findById(photoId));
                    }
                    candidates.add(candidate);
                }
            }
        } catch (Exception e) {
            LOG.error(e.getMessage(), e);
        }
        return candidates;
    }

    @Override
    public Candidate findById(int id) {
        Candidate candidate = new Candidate(0, "");
        try (Connection cn = connect(); PreparedStatement ps = cn.prepareStatement(FIND_BY_ID)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    candidate.setId(rs.getInt("id"));
                    candidate.setName(rs.getString("name"));
                    Integer photoId = rs.getObject("photo_id", Integer.class);
                    if (photoId != null) {
                        candidate.setPhoto(PHOTO_STORE.findById(photoId));
                    }
                }
            }
        } catch (Exception e) {
            LOG.error(e.getMessage(), e);
        }
        return candidate;
    }

    @Override
    public boolean delete(int id) {
        boolean result = false;
        try (Connection cn = connect(); PreparedStatement ps = cn.prepareStatement(DELETE)) {
            ps.setInt(1, id);
            Candidate candidate = findById(id);
            result = ps.executeUpdate() > 0;
            if (candidate.getPhoto() != null) {
                PHOTO_STORE.delete(candidate.getPhoto().getId());
            }
        } catch (Exception e) {
            LOG.error(e.getMessage(), e);
        }
        return result;
    }

    @Override
    public Candidate save(Candidate candidate) {
        return candidate.getId() == 0 ? create(candidate) : update(candidate);
    }

    private Candidate create(Candidate candidate) {
        try (Connection cn = connect();
             PreparedStatement ps = cn.prepareStatement(CREATE, PreparedStatement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, candidate.getName());
            if (candidate.getPhoto() != null) {
                ps.setInt(2, candidate.getPhoto().getId());
            } else {
                ps.setNull(2, Types.INTEGER);
            }
            ps.execute();
            try (ResultSet id = ps.getGeneratedKeys()) {
                if (id.next()) {
                    candidate.setId(id.getInt(1));
                }
            }
        } catch (Exception e) {
            LOG.error(e.getMessage(), e);
        }
        return candidate;
    }

    private Candidate update(Candidate candidate) {
        try (Connection cn = connect(); PreparedStatement ps = cn.prepareStatement(UPDATE)) {
            ps.setString(1, candidate.getName());
            if (candidate.getPhoto() != null) {
                ps.setInt(2, candidate.getPhoto().getId());
            } else {
                ps.setNull(2, Types.INTEGER);
            }
            ps.setInt(3, candidate.getId());
            ps.execute();
        } catch (Exception e) {
            LOG.error(e.getMessage(), e);
        }
        return candidate;
    }
}
