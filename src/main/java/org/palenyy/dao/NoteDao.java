package org.palenyy.dao;

import org.palenyy.entity.Note;

import java.util.List;

public interface NoteDao {
    List<Note> getAll();

    Note getById(Long id);

    void update(Note note);

    boolean deleteById(Long id);

    boolean insert(Note note);
}
