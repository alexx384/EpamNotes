package org.palenyy.dao;

import org.palenyy.entity.Book;

import java.util.List;

public interface BookDao {
    Book findByName(String name);

    List<Book> findAll();
}
