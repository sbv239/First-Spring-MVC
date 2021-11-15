package com.example.app.services;

import com.example.web.dto.Book;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class BookService {

    private final ProjectRepository<Book> bookRepo;

    @Autowired
    public BookService(BookRepository<Book> bookRepo) {
        this.bookRepo = bookRepo;
    }

    public List<Book> getAllBooks() {
        return bookRepo.retreiveAll();
    }

    public void saveBook(Book book) {
        bookRepo.store(book);
    }

    public boolean removeBookId(Integer bookIdtoRemove) {
        return bookRepo.removeItemById(bookIdtoRemove);
    }

    public boolean removeBookByAuthorRegex(String authorRegexToRemove) {
        return bookRepo.removeItemByAuthorRegex(authorRegexToRemove);
    }
}
