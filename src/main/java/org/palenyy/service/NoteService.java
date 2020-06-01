package org.palenyy.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.json.JsonMapper;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import com.fasterxml.jackson.dataformat.xml.ser.ToXmlGenerator;
import com.fasterxml.jackson.datatype.jdk8.Jdk8Module;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import com.fasterxml.jackson.module.paramnames.ParameterNamesModule;
import org.palenyy.dao.NoteDao;
import org.palenyy.dto.NoteDto;
import org.palenyy.entity.Note;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class NoteService {
    private final NoteDao noteDao;
    private final ObjectMapper objectMapper;
    private final XmlMapper xmlMapper;

    public NoteService(@Autowired NoteDao noteDao) {
        this.noteDao = noteDao;
        // TODO: get rid off unused modules
        JavaTimeModule javaTimeModule = new JavaTimeModule();
        LocalDateTimeDeserializer localDateTimeDeserializer = new LocalDateTimeDeserializer(
                DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"));
        LocalDateTimeSerializer localDateTimeSerializer = new LocalDateTimeSerializer(
                DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"));
        javaTimeModule.addSerializer(LocalDateTime.class, localDateTimeSerializer);
        javaTimeModule.addDeserializer(LocalDateTime.class, localDateTimeDeserializer);
        this.objectMapper = JsonMapper.builder()
                .addModule(new ParameterNamesModule())
                .addModule(new Jdk8Module())
                .addModule(javaTimeModule)
                .configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false)
                .build();
        this.xmlMapper = XmlMapper.xmlBuilder()
                .addModule(new ParameterNamesModule())
                .addModule(new Jdk8Module())
                .addModule(javaTimeModule)
                .configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false)
                .build();
        this.xmlMapper.configure( ToXmlGenerator.Feature.WRITE_XML_DECLARATION, true );
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

    @NonNull
    public String getJsonStrById(Long id) {
        return getNoteSerializedString(id, objectMapper);
    }

    public String getXmlStrById(Long id) {
        return getNoteSerializedString(id, xmlMapper);
    }

    private String getNoteSerializedString(Long id, ObjectMapper mapper) {
        NoteDto noteDto = getById(id);
        if (noteDto == null) {
            return "The element with id " + id.toString() + " does not exist";
        }
        try {
            return mapper.writeValueAsString(noteDto);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            return e.getMessage();
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

    public boolean insertNoteSerializedJson(String noteJson) {
        NoteDto noteDto;
        try {
            noteDto = objectMapper.readValue(noteJson, NoteDto.class);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            return false;
        }
        return insert(noteDto);
    }

    public boolean insertNoteSerializedXml(String noteJson) {
        NoteDto noteDto;
        try {
            noteDto = xmlMapper.readValue(noteJson, NoteDto.class);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            return false;
        }
        return insert(noteDto);
    }
}
