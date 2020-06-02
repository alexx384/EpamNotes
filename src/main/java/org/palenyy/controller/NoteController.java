package org.palenyy.controller;

import org.palenyy.dto.NoteDto;
import org.palenyy.service.NoteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

/*
TODO:
    1. Set download serialization data
    2. Make right order in history
    3. Fix formatting in history
    4. Check internalization
 */

@SuppressWarnings("SpringMVCViewInspection")
@Controller
@RequestMapping("/note")
public class NoteController {
    private final NoteService noteService;

    public NoteController(@Autowired NoteService noteService) {
        this.noteService = noteService;
    }

    @GetMapping("/")
    public String root(Model model) {
        model.addAttribute("notes", noteService.getAll());
        return "note/root";
    }

    @GetMapping("/new")
    public String getNewNote() {
        return "note/new";
    }

    @PostMapping("/new")
    public String postNewNote(@RequestParam("heading") String heading, @RequestParam("text") String text) {
        noteService.insert(new NoteDto(heading, text));
        return "note/new";
    }

    @GetMapping("/edit/{key}")
    public String getEditNote(@PathVariable("key") long key, Model model) {
        NoteDto noteDto = noteService.getById(key);
        if (noteDto == null) {
            return "note/error";
        }
        model.addAttribute("note", noteDto);
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
        if (noteService.deleteById(key)) {
            return root(model);
        } else {
            return "note/error";
        }
    }

    @GetMapping("/view/{key}")
    public String getViewNote(@PathVariable("key") long key, Model model) {
        NoteDto noteDto = noteService.getById(key);
        if (noteDto == null) {
            return "note/error";
        }
        model.addAttribute("note", noteDto);
        return "note/view";
    }

    @ResponseBody
    @GetMapping("/json/{key}")
    public String getNoteJsonRepresentation(@PathVariable("key") long key) {
        return noteService.getJsonStrById(key);
    }

    @ResponseBody
    @GetMapping("/xml/{key}")
    public String getNoteXmlRepresentation(@PathVariable("key") long key) {
        return noteService.getXmlStrById(key);
    }

    @PostMapping("/upload/json")
    public String insertNoteJson(@RequestParam("jsonNote") MultipartFile fileJsonNote, Model model) {
        String strJsonNote;
        try {
            byte[] contentJsonNote = fileJsonNote.getBytes();
            strJsonNote = new String(contentJsonNote);
        } catch (IOException e) {
            e.printStackTrace();
            return "note/error";
        }
        if (noteService.insertNoteSerializedJson(strJsonNote)) {
            return root(model);
        } else {
            return "note/error";
        }
    }

    @PostMapping("/upload/xml")
    public String insertNoteXml(@RequestParam("xmlNote") MultipartFile fileXmlNote, Model model) {
        String strXmlNote;
        try {
            byte[] contentXmlNote = fileXmlNote.getBytes();
            strXmlNote = new String(contentXmlNote);
        } catch (IOException e) {
            e.printStackTrace();
            return "note/error";
        }
        if (noteService.insertNoteSerializedXml(strXmlNote)) {
            return root(model);
        } else {
            return "note/error";
        }
    }

    @GetMapping("/history/{key}")
    public String getHistoryById(@PathVariable("key") long key, Model model) {
        model.addAttribute("notes", noteService.getHistoryById(key));
        return "note/history";
    }
}
