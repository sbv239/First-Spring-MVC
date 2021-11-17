package com.example.app.services;

import com.example.web.dto.Book;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.logging.Logger;

@Service
public class BookService {

    private final ProjectRepository<Book> bookRepo;
    private final Logger logger = Logger.getLogger(String.valueOf(BookService.class));

    @Autowired
    public BookService(BookRepository<Book> bookRepo) {
        this.bookRepo = bookRepo;
    }

    public List<Book> getAllBooks() {
        return bookRepo.retreiveAll();
    }

    public void saveBook(Book book) {
        if (book.getAuthor().equals("") && book.getSize() == null && book.getTitle().equals("")) {
            logger.info("can't save empty book");
        } else {
            bookRepo.store(book);
        }
    }

    public boolean removeBookId(Integer bookIdtoRemove) {
        return bookRepo.removeItemById(bookIdtoRemove);
    }

    public void removeBookByAuthorRegex(String authorRegexToRemove) {
        bookRepo.removeItemByAuthorRegex(authorRegexToRemove);
    }
}
