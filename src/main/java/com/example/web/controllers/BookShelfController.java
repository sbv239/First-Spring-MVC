package com.example.web.controllers;

import com.example.app.exceptions.FileNotChoosenException;
import com.example.app.services.BookService;
import com.example.web.dto.Book;
import com.example.web.dto.BookIdToRemove;
import com.example.web.dto.BookRegexToRemove;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.util.Objects;
import java.util.logging.Logger;

@Controller
@RequestMapping(value = "books")
@Scope("singleton")
public class BookShelfController {
    private Logger logger = Logger.getLogger(String.valueOf(BookShelfController.class));
    private BookService bookService;

    @Autowired
    public BookShelfController(BookService bookService) {
        this.bookService = bookService;
    }

    @GetMapping("/shelf")
    public String books(Model model) {
        logger.info(this.toString());
        model.addAttribute("book", new Book());
        model.addAttribute("bookIdToRemove", new BookIdToRemove());
        model.addAttribute("bookList", bookService.getAllBooks());
        model.addAttribute("bookRegexToRemove", new BookRegexToRemove());
        return "book_shelf";
    }

    @PostMapping("/save")
    public String saveBook(@Valid Book book, BindingResult bindingResult, Model model) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("book", book);
            model.addAttribute("bookIdToRemove", new BookIdToRemove());
            model.addAttribute("bookList", bookService.getAllBooks());
            model.addAttribute("bookRegexToRemove", new BookRegexToRemove());
            return "book_shelf";
        } else {
            bookService.saveBook(book);
            logger.info("current repository size: " + bookService.getAllBooks().size());
            return "redirect:/books/shelf";
        }
    }

    @PostMapping("/remove")
    public String removeBook(@Valid BookIdToRemove bookIdToRemove, BindingResult bindingResult, Model model) {

        if (bindingResult.hasErrors()) {
            model.addAttribute("book", new Book());
            model.addAttribute("bookIdToRemove", bookIdToRemove);
            model.addAttribute("bookList", bookService.getAllBooks());
            model.addAttribute("bookRegexToRemove", new BookRegexToRemove());
            return "book_shelf";
        } else {
            bookService.removeBookId(bookIdToRemove.getId());
            return "redirect:/books/shelf";
        }
    }

    @PostMapping("/removeByRegex")
    public String removeBook(@Valid BookRegexToRemove bookRegexToRemove, BindingResult bindingResult, Model model) {

        if (bindingResult.hasErrors()) {
            model.addAttribute("book", new Book());
            model.addAttribute("bookIdToRemove", new BookIdToRemove());
            model.addAttribute("bookList", bookService.getAllBooks());
            model.addAttribute("bookRegexToRemove", bookRegexToRemove);
            return "book_shelf";
        } else {
            bookService.removeBookByRegex(bookRegexToRemove);
            return "redirect:/books/shelf";
        }
    }

    @PostMapping("/uploadFile")
    public String uploadFile(@RequestParam("file") MultipartFile file) throws Exception {
        if (Objects.equals(file.getOriginalFilename(), "")) {
            throw new FileNotChoosenException("Where is a file?");
        }
        String name = file.getOriginalFilename();
        byte[] bytes = file.getBytes();

        //create dir
        String rootPath = System.getProperty("catalina.home");
        File dir = new File(rootPath + File.separator + "external_uploads");
        if (!dir.exists()) {
            dir.mkdirs();
        }

        //create file
        File serverFile = new File(dir.getAbsolutePath() + File.separator + name);
        BufferedOutputStream stream = new BufferedOutputStream(new FileOutputStream(serverFile));
        stream.write(bytes);
        stream.close();

        logger.info("new file saved at: " + serverFile.getAbsolutePath());

        return "redirect:/books/shelf";
    }

    @ExceptionHandler(FileNotChoosenException.class)
    public String handleError(Model model, FileNotChoosenException exception) {
        model.addAttribute("errorMessage", exception.getMessage());
        return "redirect:/books/shelf";
    }
}