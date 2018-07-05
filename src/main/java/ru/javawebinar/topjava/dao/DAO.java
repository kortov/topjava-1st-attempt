package ru.javawebinar.topjava.dao;

import java.util.List;

public interface DAO<T> {
    void addItem(T item);

    void updateItem(T item);

    void removeItem(T item);

    void removeItem(long id);

    T getItem(long id);

    List<T> getAll();
}
