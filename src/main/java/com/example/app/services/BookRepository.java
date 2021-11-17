package com.example.app.services;

import com.example.web.dto.Book;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

@Repository
public class BookRepository<T> implements ProjectRepository<Book> {

    private final Logger logger = Logger.getLogger(String.valueOf(BookRepository.class));
    private final List<Book> repo = new ArrayList<>();

    @Override
    public List<Book> retreiveAll() {
        return new ArrayList<>(repo);
    }

    @Override
    public void store(Book book) {
        book.setId(book.hashCode());
        logger.info("store new book: " + book);
        repo.add(book);
    }

    @Override
    public boolean removeItemById(Integer bookIdtoRemove) {
        for (Book book : retreiveAll()) {
            if (book.getId().equals(bookIdtoRemove)) {
                logger.info("remove book completed: " + book);
                return repo.remove(book);
            }
        }
        return false;
    }

    public void removeItemByAuthorRegex(String regex) {
        if (regex != null && regex.isEmpty()) {
            logger.info("empty regex");
        }
        int size = repo.size();
        for (Book book : retreiveAll()) {
            if (book.getAuthor().contains(regex)) {
                logger.info("remove book completed: " + book);
                repo.remove(book);
            }
        }
        if (repo.size() == size) {
            logger.info("can't find such regex: " + regex);
        }
    }
}
