package org.palenyy.dao;

import org.palenyy.entity.Book;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.util.List;

@Repository
public class BookDaoImpl implements BookDao {

    private final JdbcTemplate jdbcTemplate;

    BookDaoImpl(@Autowired JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public Book findByName(String name) {
        String sql = "SELECT * FROM books WHERE name=:name";
        Book result = jdbcTemplate.queryForObject(
                sql,
                new Object[]{name},
                (ResultSet rs, int rowNum) -> new Book(
                        rs.getLong("ID"),
                        rs.getString("NAME")
                )
        );
        return null;
    }

    @Override
    public List<Book> findAll() {
        String sql = "SELECT * FROM books";
        return jdbcTemplate.query(sql, (ResultSet rs, int rowNum) -> new Book(
                rs.getLong("ID"),
                rs.getString("NAME")
        ));
    }
}
