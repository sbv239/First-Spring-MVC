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
        System.out.println(book);
        if (book.getAuthor().equals("") && book.getSize() == null && book.getTitle().equals("")) {
            logger.info("can't save empty book");
        } else {
            book.setId(book.hashCode());
            logger.info("store new book: " + book);
            repo.add(book);
        }
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

    @Override
    public boolean removeItemByAuthorRegex(String authorRegexToRemove) {
        if (authorRegexToRemove.equals("")) {
            return false;
        }
        int i = 0;
        for (Book book : retreiveAll()) {
            if (book.getAuthor().contains(authorRegexToRemove)) {
                logger.info("remove book completed: " + book);
                repo.remove(book);
                i++;
            }
        }
        return i > 0;
    }
}
