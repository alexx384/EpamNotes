package org.palenyy.controllers;

import org.palenyy.Note;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

@Controller
@RequestMapping("/note")
public class NoteController {
    @GetMapping("/")
    public String root(Model model) {
        Note note1 = new Note("Heading1", "Text1", LocalDateTime.now(), LocalDateTime.now());
        Note note2 = new Note("Heading2", "Text2", LocalDateTime.now(), LocalDateTime.now());
        List<Note> noteList = Arrays.asList(note1, note2);
        model.addAttribute("notes", noteList);
        return "note/root";
    }

    @GetMapping("/new")
    public String newNote() {
        return "note/new";
    }

    @GetMapping("/edit")
    public String editNote() {
        return "note/edit";
    }
}
