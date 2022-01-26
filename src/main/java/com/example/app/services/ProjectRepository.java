package com.example.app.services;

import com.example.web.dto.BookRegexToRemove;

import java.util.List;

public interface ProjectRepository<T> {
    List<T> retreiveAll();

    void store(T book);

    boolean removeItemById(Integer bookIdToRemove);

    void removeItemByRegex(BookRegexToRemove bookRegexToRemove);
}
