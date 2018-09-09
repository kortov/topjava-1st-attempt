package ru.javawebinar.topjava.service;


import ru.javawebinar.topjava.model.*;
import ru.javawebinar.topjava.util.exception.*;

import java.util.*;

public interface UserService {

    User create(User user);

    void delete(int id) throws NotFoundException;

    User get(int id) throws NotFoundException;

    User getByEmail(String email) throws NotFoundException;

    void update(User user);

    List<User> getAll();

    User getWithMeals(int id);
}