package com.example.app.services;

import java.util.List;

public interface ProjectRepository<T> {
    List<T> retreiveAll();

    void store(T book);

    boolean removeItemById(Integer bookIdtoRemove);

    void removeItemByAuthorRegex(String authorRegexToRemove);
}
