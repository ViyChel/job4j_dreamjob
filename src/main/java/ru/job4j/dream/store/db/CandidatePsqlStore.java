package ru.job4j.dream.store.db;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import ru.job4j.dream.model.Candidate;
import ru.job4j.dream.store.Store;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

/**
 * Class CandidatePsqlStore.
 *
 * @author Vitaly Yagufarov (for.viy@gmail.com)
 * @version 1.0
 * @since 12.11.2020
 */
public class CandidatePsqlStore implements Store<Candidate> {
    private static final ConnectorDB CONNECTOR_DB = ConnectorDB.getInstance();
    private static final Log LOG = LogFactory.getLog(CandidatePsqlStore.class.getName());

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
        try (Connection cn = connect();
             PreparedStatement ps = cn.prepareStatement("SELECT * FROM candidates;")
        ) {
            try (ResultSet it = ps.executeQuery()) {
                while (it.next()) {
                    candidates.add(new Candidate(it.getInt("id"), it.getString("name")));
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
        try (Connection cn = connect();
             PreparedStatement ps = cn.prepareStatement("SELECT * FROM candidates WHERE id = ?;")
        ) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    candidate.setId(rs.getInt("id"));
                    candidate.setName(rs.getString("name"));
                }
            }
        } catch (Exception e) {
            LOG.error(e.getMessage(), e);
        }
        return candidate;
    }

    @Override
    public void save(Candidate candidate) {
        if (candidate.getId() == 0) {
            create(candidate);
        } else {
            update(candidate);
        }
    }

    private Candidate create(Candidate candidate) {
        try (Connection cn = connect();
             PreparedStatement ps = cn.prepareStatement("INSERT INTO candidates (name) VALUES (?);",
                     PreparedStatement.RETURN_GENERATED_KEYS)
        ) {
            ps.setString(1, candidate.getName());
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

    private void update(Candidate candidate) {
        try (Connection cn = connect();
             PreparedStatement ps = cn.prepareStatement("UPDATE candidates SET name = ?  WHERE id = ?;")
        ) {
            ps.setString(1, candidate.getName());
            ps.setInt(2, candidate.getId());
            ps.execute();
        } catch (Exception e) {
            LOG.error(e.getMessage(), e);
        }
    }
}
