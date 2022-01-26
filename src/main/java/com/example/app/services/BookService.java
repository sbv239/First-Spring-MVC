package com.example.app.services;

import com.example.web.dto.Book;
import com.example.web.dto.BookRegexToRemove;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class BookService {

    private final ProjectRepository<Book> bookRepo;
    private final Logger logger = Logger.getLogger(BookService.class);

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

    public boolean removeBookId(Integer bookIdToRemove) {
        return bookRepo.removeItemById(bookIdToRemove);
    }

    public void removeBookByRegex(BookRegexToRemove bookRegexToRemove) {
        bookRepo.removeItemByRegex(bookRegexToRemove);
    }
}
