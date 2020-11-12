package ru.job4j.dream.store.db;

import ru.job4j.dream.model.Candidate;
import ru.job4j.dream.model.Post;
import ru.job4j.dream.store.Store;

/**
 * Class PsqlMain.
 *
 * @author Vitaly Yagufarov (for.viy@gmail.com)
 * @version 1.0
 * @since 10.11.2020
 */
public class PsqlMain {
    public static void main(String[] args) {
        // Post
        // create() and findAll() checking
        Store<Post> postStore = PostPsqlStore.instOf();
        postStore.save(new Post(0, "Java Job", "First level"));
        postStore.findAll().forEach(System.out::println);
        System.out.println();

        // update() checking
        Post post = new Post(1, "New name", "New description");
        postStore.save(post);
        postStore.findAll().forEach(System.out::println);
        System.out.println();

        // findById() checking
        System.out.println(postStore.findById(post.getId()));
        System.out.println();

        // Candidate
        // create() and findAll() checking
        Store<Candidate> candidateStore = CandidatePsqlStore.instOf();
        candidateStore.save(new Candidate(0, "Ivan Ivanov"));
        candidateStore.findAll().forEach(System.out::println);
        System.out.println();

        // update() checking
        Candidate candidate = new Candidate(0, "Semen Semenov");
        candidateStore.save(candidate);
        candidateStore.findAll().forEach(System.out::println);
        System.out.println();

        // findById() checking
        System.out.println(candidateStore.findById(candidate.getId()));
    }
}
