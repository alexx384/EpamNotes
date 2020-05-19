package org.palenyy;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class NoteStorage {
    private final List<Note> noteList;

    public NoteStorage() {
        noteList = new ArrayList<>();
    }

    public void addNote(String heading, String text) {
        Note note = Note.builder(heading, text);
        Objects.requireNonNull(note);
        noteList.add(note);
    }

    public List<Note> getNoteList() {
        return new ArrayList<>(noteList);
    }
}
