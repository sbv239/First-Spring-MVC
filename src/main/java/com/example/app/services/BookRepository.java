package com.example.app.services;

import com.example.web.dto.Book;
import com.example.web.dto.BookRegexToRemove;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.util.List;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Repository
public class BookRepository<T> implements ProjectRepository<Book>, ApplicationContextAware {

    private final Logger logger = Logger.getLogger(String.valueOf(BookRepository.class));
    private ApplicationContext context;

    private NamedParameterJdbcTemplate jdbcTemplate;

    @Autowired
    public BookRepository(NamedParameterJdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public List<Book> retreiveAll() {
        return jdbcTemplate.query("SELECT * FROM books", (ResultSet rs, int rowNum) -> {
        Book book = new Book();
        book.setId(rs.getInt("id"));
        book.setAuthor(rs.getString("author"));
        book.setTitle(rs.getString("title"));
        book.setSize(rs.getInt("size"));
        return book;
       });
    }

    @Override
    public void store(Book book) {
        MapSqlParameterSource parameterSource = new MapSqlParameterSource();
        parameterSource.addValue("author", book.getAuthor());
        parameterSource.addValue("title", book.getTitle());
        parameterSource.addValue("size", book.getSize());
        jdbcTemplate.update("INSERT INTO books(author, title, size) VALUES(:author, :title, :size)", parameterSource);
        logger.info("store new book: " + book);

    }

    @Override
    public boolean removeItemById(Integer bookIdtoRemove) {
        MapSqlParameterSource parameterSource = new MapSqlParameterSource();
        parameterSource.addValue("id", bookIdtoRemove);
        jdbcTemplate.update("DELETE FROM books WHERE id = :id", parameterSource);
        logger.info("remove book completed: ");
        return true;
    }

    public void removeItemByRegex(BookRegexToRemove bookRegexToRemove) {

        if (bookRegexToRemove.getRegex().isEmpty()) {
            logger.info("empty regex");
            return;
        }
        int size = retreiveAll().size();
        Pattern patternToDelete = Pattern.compile(bookRegexToRemove.getRegex());
        MapSqlParameterSource parameterSource = new MapSqlParameterSource();
        for (Book book : retreiveAll()) {
            Matcher matcher = patternToDelete.matcher(book.getAuthor());
            Matcher matcher1 = patternToDelete.matcher(book.getTitle());
            Matcher matcher2 = patternToDelete.matcher(String.valueOf(book.getSize()));

            if (matcher.find() || matcher1.find() || matcher2.find()) {
                parameterSource.addValue("id", book.getId());
                jdbcTemplate.update("DELETE FROM books WHERE id = :id", parameterSource);
                logger.info("remove book completed: " + book);
            }
        }
        if (retreiveAll().size() == size) {
            logger.info("can't find such regex: " + bookRegexToRemove.getRegex());
        }
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.context = applicationContext;
    }
}
