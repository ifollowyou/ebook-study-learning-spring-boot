package org.test.bookpub.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.StringTrimmerEditor;
import org.springframework.util.Assert;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;
import org.test.bookpub.entity.Book;
import org.test.bookpub.entity.Publisher;
import org.test.bookpub.entity.Reviewer;
import org.test.bookpub.repository.BookRepository;
import org.test.bookpub.repository.PublisherRepository;

import javax.servlet.http.HttpServletRequest;
import java.util.Collections;
import java.util.List;

@RestController
@RequestMapping("/books")
public class BookController {
    @Autowired
    private BookRepository bookRepository;

    @InitBinder
    public void initBinder(WebDataBinder binder) {
        binder.registerCustomEditor(String.class, new StringTrimmerEditor(false));
    }

    @RequestMapping(value = "", method = RequestMethod.GET)
    public Iterable<Book> getAllBooks() {
        return bookRepository.findAll();
    }

    @RequestMapping(value = "/{isbn}", method = RequestMethod.GET)
    public Book getBook(@PathVariable String isbn) {
        return bookRepository.findBookByIsbn(isbn);
    }

    @RequestMapping(value = "/{isbn}/reviewers", method = RequestMethod.GET)
    public List<Reviewer> getReviewers(@PathVariable("isbn") Book book) {
        return book.getReviewers();
    }

    @RequestMapping(value = "/session", method = RequestMethod.GET)
    public String getSessionId(HttpServletRequest request) {
        return request.getSession().getId();
    }

    @Autowired
    private PublisherRepository publisherRepository;

    @RequestMapping(value = "/publisher/{id}", method = RequestMethod.GET)
    public List<Book> getBooksByPublisher(@PathVariable("id") Long id) {
        Publisher publisher = publisherRepository.findOne(id);
        Assert.notNull(publisher);
        return publisher.getBooks();
    }
}