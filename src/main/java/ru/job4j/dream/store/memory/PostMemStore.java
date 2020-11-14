package ru.job4j.dream.store.memory;

import ru.job4j.dream.model.Post;
import ru.job4j.dream.store.Store;

import java.util.Collection;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Class Store.
 *
 * @author Vitaly Yagufarov (for.viy@gmail.com)
 * @version 1.0
 * @since 06.11.2020
 */
public class PostMemStore implements Store<Post> {

    private static final PostMemStore INST = new PostMemStore();
    private Map<Integer, Post> posts = new ConcurrentHashMap<>();
    private static AtomicInteger postID = new AtomicInteger(4);

    private PostMemStore() {
        posts.put(1, new Post(1, "Junior Java Job", "Junior description"));
        posts.put(2, new Post(2, "Middle Java Job", "Middle description"));
        posts.put(3, new Post(3, "Senior Java Job", "Senior description"));
    }

    /**
     * Inst of post mem store.
     *
     * @return the post mem store
     */
    public static PostMemStore instOf() {
        return INST;
    }

    @Override
    public Collection<Post> findAll() {
        return this.posts.values();
    }

    @Override
    public Post save(Post post) {
        if (post.getId() == 0) {
            post.setId(postID.incrementAndGet());
        }
        this.posts.put(post.getId(), post);
        return post;
    }

    @Override
    public Post findById(int id) {
        return this.posts.get(id);
    }

    @Override
    public boolean delete(int id) {
        return this.posts.remove(id) != null;
    }
}