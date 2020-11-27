package ru.job4j.dream.servlet;

import ru.job4j.dream.model.User;

import java.util.List;

/**
 * Interface Validate.
 *
 * @author Vitaly Yagufarov (for.viy@gmail.com)
 * @version 1.0
 * @since 27.11.2020
 */
public interface Validate {
    User add(User model);

    List<User> getAll();
}
