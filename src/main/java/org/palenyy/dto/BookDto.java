package org.palenyy.dto;

public class BookDto {
    private final Long id;
    private final String name;

    public BookDto(Long id, String name) {
        this.id = id;
        this.name = name;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    @Override
    public String toString() {
        return "BookDto{" +
                "id=" + id +
                ", name='" + name + '\'' +
                '}';
    }
}
