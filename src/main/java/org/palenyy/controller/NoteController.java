package org.palenyy.controller;

import org.palenyy.dto.NoteDto;
import org.palenyy.service.NoteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/note")
public class NoteController {
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
}
