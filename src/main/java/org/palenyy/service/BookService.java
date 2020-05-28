package org.palenyy.service;

import org.palenyy.dao.BookDao;
import org.palenyy.dto.BookDto;
import org.palenyy.entity.Book;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class BookService {
    private final BookDao bookDao;

    public BookService(@Autowired BookDao bookDao) {
        this.bookDao = bookDao;
    }

    public BookDto findByName(String name) {
        Book book = bookDao.findByName(name);
        if (book != null) {
            return new BookDto(book.getId(), book.getName());
        } else {
            return null;
        }
    }

    public List<BookDto> findAll() {
        return bookDao.findAll().stream()
                .map(book -> new BookDto(book.getId(), book.getName()))
                .collect(Collectors.toList());
    }
}
