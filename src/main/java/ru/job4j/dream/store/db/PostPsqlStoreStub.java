package ru.job4j.dream.store.db;

import ru.job4j.dream.model.Post;
import ru.job4j.dream.store.Store;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Class PostPsqlStoreStub.
 *
 * @author Vitaly Yagufarov (for.viy@gmail.com)
 * @version 1.0
 * @since 30.11.2020
 */
public class PostPsqlStoreStub implements Store<Post> {
    private static final PostPsqlStoreStub INST = new PostPsqlStoreStub();
    private Map<Integer, Post> posts = new ConcurrentHashMap<>();
    private static AtomicInteger postID = new AtomicInteger(1);

    public static PostPsqlStoreStub getINST() {
        return INST;
    }

    @Override
    public Collection<Post> findAll() {
        return new ArrayList<>(this.posts.values());
    }

    @Override
    public Post save(Post model) {
        model.setId(postID.getAndIncrement());
        this.posts.put(model.getId(), model);
        return model;
    }

    @Override
    public Post findById(int id) {
        return null;
    }

    @Override
    public boolean delete(int id) {
        return false;
    }
}
