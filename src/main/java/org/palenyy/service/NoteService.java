package org.palenyy.service;

import org.palenyy.dao.NoteDao;
import org.palenyy.dto.NoteDto;
import org.palenyy.entity.Note;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class NoteService {
    private final NoteDao noteDao;

    public NoteService(@Autowired NoteDao noteDao) {
        this.noteDao = noteDao;
    }

    public List<NoteDto> getAll() {
        return noteDao.getAll().stream()
                .map(note -> new NoteDto(
                        note.getId(),
                        note.getHeading(),
                        note.getText(),
                        note.getLastEditDateTime(),
                        note.getCreationDateTime()))
                .collect(Collectors.toList());
    }

    public NoteDto getById(Long id) {
        Note note = noteDao.getById(id);
        if (note != null) {
            return new NoteDto(
                    note.getId(),
                    note.getHeading(),
                    note.getText(),
                    note.getLastEditDateTime(),
                    note.getCreationDateTime()
            );
        } else {
            return null;
        }
    }

    public void update(NoteDto noteDto) {
        noteDao.update(new Note(
                noteDto.getId(),
                noteDto.getHeading(),
                noteDto.getText(),
                LocalDateTime.now(),
                noteDto.getCreationDateTime()
            )
        );
    }

    public boolean deleteById(Long id) {
        return noteDao.deleteById(id);
    }

    public boolean insert(NoteDto noteDto) {
        LocalDateTime localDateTime = LocalDateTime.now();
        return noteDao.insert(new Note(
                noteDto.getHeading(),
                noteDto.getText(),
                localDateTime,
                localDateTime
            )
        );
    }
}
