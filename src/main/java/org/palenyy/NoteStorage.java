package org.palenyy;

import java.util.*;

public class NoteStorage {
    private static final int IDX_LOWER_BOUND = 1;
    private int lastValidIdx = IDX_LOWER_BOUND;
    private final Map<Integer, Note> noteMap;

    public NoteStorage() {
        noteMap = new LinkedHashMap<>();
    }

    private void findNextValidIdx() {
        //noinspection StatementWithEmptyBody
        while (noteMap.containsKey(++lastValidIdx));
    }

    public void addNote(String heading, String text) {
        Note note = Note.builder(heading, text);
        Objects.requireNonNull(note);
        noteMap.put(lastValidIdx, note);
        findNextValidIdx();
    }

    public void editNote(Integer key, String heading, String text) {
        Note note = noteMap.get(key);
        Objects.requireNonNull(note);
        note.setHeading(heading);
        note.setText(text);
        noteMap.put(key, note);
    }

    public void deleteNote(Integer key) {
        noteMap.remove(key);
        lastValidIdx = key;
    }

    public Set<Map.Entry<Integer, Note>> getNoteMapEntries() {
        return noteMap.entrySet();
    }
}
