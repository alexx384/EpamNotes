package org.palenyy.controllers;

import org.palenyy.NoteStorage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/note")
public class NoteController {
    @Autowired
    private NoteStorage noteStorage;

    @GetMapping("/")
    public String root(Model model) {
        model.addAttribute("notes", noteStorage.getNoteList());
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
        noteStorage.addNote(heading, text);
        //noinspection SpringMVCViewInspection
        return "note/new";
    }

    @GetMapping("/edit")
    public String editNote() {
        //noinspection SpringMVCViewInspection
        return "note/edit";
    }
}
