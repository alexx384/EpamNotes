package org.palenyy.controllers;

import org.palenyy.NoteStorage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

// TODO: 1. How to conviniently get access to ApplicationContext

@Controller
@RequestMapping("/note")
public class NoteController {
    @Autowired
    private NoteStorage noteStorage;

    @GetMapping("/")
    public String root(Model model) {
        model.addAttribute("notes", noteStorage.getNoteMapEntries());
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

    @GetMapping("/edit/**")
    public String getEditNote() {
        //noinspection SpringMVCViewInspection
        return "note/edit";
    }

    @PostMapping("/edit/{key}")
    public String postEditNote(@PathVariable("key") int key, @RequestParam("heading") String heading,
                               @RequestParam("text") String text) {
        noteStorage.editNote(key, heading, text);
        return "note/edit";
    }

    @GetMapping("/delete/{key}")
    public String postDeleteNote(@PathVariable("key") int key, Model model) {
        noteStorage.deleteNote(key);
        return root(model);
    }
}
