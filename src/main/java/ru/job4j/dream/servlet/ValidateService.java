package ru.job4j.dream.servlet;

import ru.job4j.dream.model.User;

import java.util.ArrayList;
import java.util.List;

/**
 * Class ValidateService.
 *
 * @author Vitaly Yagufarov (for.viy@gmail.com)
 * @version 1.0
 * @since 27.11.2020
 */
public class ValidateService implements Validate{
    private static final Validate SERVICE = new ValidateService();
    private final List<User> list  = new ArrayList<>();

    public static Validate getInstance() {
        return SERVICE;
    }

    @Override
    public User add(User model) {
        this.list.add(model);
        return model;
    }

    @Override
    public List<User> getAll() {
        return list;
    }
}
