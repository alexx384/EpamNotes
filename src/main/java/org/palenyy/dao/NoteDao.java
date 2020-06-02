package org.palenyy.dao;

import org.palenyy.entity.Note;

import java.util.List;

public interface NoteDao {
    List<Note> getAll();

    List<Note> getHistoryById(Long id);

    Note getById(Long id);

    void update(Note note);

    boolean deleteById(Long id);

    void insert(Note note);
}
