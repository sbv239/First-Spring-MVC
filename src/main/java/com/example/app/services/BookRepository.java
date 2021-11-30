package com.example.app.services;

import com.example.web.dto.Book;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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

    public void removeItemByRegex(String regex) {
        if (regex != null && regex.isEmpty()) {
            logger.info("empty regex");
        }
        int size = repo.size();
        Pattern patternToDelete = Pattern.compile(regex);
        for (Book book : retreiveAll()) {
            Matcher matcher = patternToDelete.matcher(book.getAuthor());
            Matcher matcher1 = patternToDelete.matcher(book.getTitle());
            Matcher matcher2 = patternToDelete.matcher(String.valueOf(book.getSize()));

            if (matcher.find() || matcher1.find() || matcher2.find()) {
                logger.info("remove book completed: " + book);
                repo.remove(book);
            }
        }
        if (repo.size() == size) {
            logger.info("can't find such regex: " + regex);
        }
    }
}
