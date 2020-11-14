package ru.job4j.dream.store.memory;

import ru.job4j.dream.model.Candidate;
import ru.job4j.dream.store.Store;

import java.util.Collection;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Class CandidatePostMem.
 *
 * @author Vitaly Yagufarov (for.viy@gmail.com)
 * @version 1.0
 * @since 11.11.2020
 */
public class CandidateMemStore implements Store<Candidate> {

    private static final CandidateMemStore INST = new CandidateMemStore();
    private Map<Integer, Candidate> candidates = new ConcurrentHashMap<>();
    private static AtomicInteger candidateID = new AtomicInteger(4);

    private CandidateMemStore() {
        candidates.put(1, new Candidate(1, "Junior Java"));
        candidates.put(2, new Candidate(2, "Middle Java"));
        candidates.put(3, new Candidate(3, "Senior Java"));
    }

    /**
     * Inst of candidate mem store.
     *
     * @return the candidate mem store
     */
    public static CandidateMemStore instOf() {
        return INST;
    }

    @Override
    public Collection<Candidate> findAll() {
        return this.candidates.values();
    }

    @Override
    public Candidate save(Candidate candidate) {
        if (candidate.getId() == 0) {
            candidate.setId(candidateID.incrementAndGet());
        }
        this.candidates.put(candidate.getId(), candidate);
        return candidate;
    }

    @Override
    public Candidate findById(int id) {
        return this.candidates.get(id);
    }

    @Override
    public boolean delete(int id) {
        return this.candidates.remove(id) != null;
    }
}
