package ru.job4j.dream.servlet;

import ru.job4j.dream.model.User;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Class ValidateStub.
 *
 * @author Vitaly Yagufarov (for.viy@gmail.com)
 * @version 1.0
 * @since 27.11.2020
 */
public class ValidateStub implements Validate {
    private final Map<Integer, User> store = new HashMap<>();
    private int ids = 0;

    @Override
    public User add(User model) {
        model.setId(this.ids++);
        this.store.put(model.getId(), model);
        return model;
    }

    @Override
    public List<User> getAll() {
        return new ArrayList<>(this.store.values());
    }
}
