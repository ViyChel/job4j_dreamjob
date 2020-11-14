package ru.job4j.dream.store;

import java.util.Collection;

/**
 * Interface Store.
 *
 * @author Vitaly Yagufarov (for.viy@gmail.com)
 * @version 1.0
 * @since 10.11.2020
 */
public interface Store<T> {
    Collection<T> findAll();

    T save(T model);

    T findById(int id);

    boolean delete(int id);
}
