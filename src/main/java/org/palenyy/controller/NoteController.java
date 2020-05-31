package org.palenyy.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import com.fasterxml.jackson.dataformat.xml.ser.ToXmlGenerator;
import org.palenyy.dto.NoteDto;
import org.palenyy.service.NoteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/note")
public class NoteController {
    private final ObjectMapper objectMapper;
    private final XmlMapper xmlMapper;

    {
        objectMapper = new ObjectMapper();
        xmlMapper = new XmlMapper();
        xmlMapper.configure( ToXmlGenerator.Feature.WRITE_XML_DECLARATION, true );
    }

    @Autowired
    private NoteService noteService;

    @GetMapping("/")
    public String root(Model model) {
        model.addAttribute("notes", noteService.getAll());
        //noinspection SpringMVCViewInspection
        return "note/root";
    }

    @GetMapping("/new")
    public String getNewNote() {
        //noinspection SpringMVCViewInspection
        return "note/new";
    }

    @PostMapping("/new")
    public String postNewNote(@RequestParam("heading") String heading, @RequestParam("text") String text) {
        noteService.insert(new NoteDto(heading, text));
        //noinspection SpringMVCViewInspection
        return "note/new";
    }

    @GetMapping("/edit/{key}")
    public String getEditNote(@PathVariable("key") long key, Model model) {
        NoteDto noteDto = noteService.getById(key);
        if (noteDto == null) {
            return "note/error";
        }
        model.addAttribute("note", noteDto);
        //noinspection SpringMVCViewInspection
        return "note/edit";
    }

    @PostMapping("/edit/{key}")
    public String postEditNote(@PathVariable("key") long key, @RequestParam("heading") String heading,
                               @RequestParam("text") String text, Model model) {
        noteService.update(new NoteDto(key, heading, text));
        return getEditNote(key, model);
    }

    @GetMapping("/delete/{key}")
    public String postDeleteNote(@PathVariable("key") long key, Model model) {
        noteService.deleteById(key);
        return root(model);
    }

    @GetMapping("/view/{key}")
    public String getViewNote(@PathVariable("key") long key, Model model) {
        NoteDto noteDto = noteService.getById(key);
        if (noteDto == null) {
            return "note/error";
        }
        model.addAttribute("note", noteDto);
        //noinspection SpringMVCViewInspection
        return "note/view";
    }

    @ResponseBody
    @GetMapping("/json/{key}")
    public String getNoteJsonRepresentation(@PathVariable("key") long key) {
        NoteDto noteDto = noteService.getById(key);
        if (noteDto == null) {
            return "note/error";
        }
        try {
            return objectMapper.writeValueAsString(noteDto);
        } catch (JsonProcessingException e) {
            return e.getMessage();
        }
    }

    @ResponseBody
    @GetMapping("/xml/{key}")
    public String getNoteXmlRepresentation(@PathVariable("key") long key) {
        NoteDto noteDto = noteService.getById(key);
        if (noteDto == null) {
            return "note/error";
        }
        try {
            return xmlMapper.writeValueAsString(noteDto);
        } catch (JsonProcessingException e) {
            return e.getMessage();
        }
    }
}
