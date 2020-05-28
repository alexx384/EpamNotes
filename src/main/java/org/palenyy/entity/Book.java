package org.palenyy.entity;

public class Book {
    private final Long id;
    private final String name;

    public Book(long id, String name) {
        this.id = id;
        this.name = name;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }
}
