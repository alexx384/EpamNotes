package org.palenyy.controller;

import org.palenyy.dto.NoteDto;
import org.palenyy.service.NoteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.nio.charset.StandardCharsets;

// TODO: Think about character encoding at https://stackoverflow.com/questions/33941751/html-form-does-not-send-utf-8-format-inputs?lq=1
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

    @PostMapping(value = "/new")
    public String postNewNote(@RequestParam("heading") String heading, @RequestParam("text") String text) {
        String crutchHeading = new String(heading.getBytes(StandardCharsets.ISO_8859_1));
        String crutchText = new String(text.getBytes(StandardCharsets.ISO_8859_1));
        noteService.insert(new NoteDto(crutchHeading, crutchText));
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
        String crutchHeading = new String(heading.getBytes(StandardCharsets.ISO_8859_1));
        String crutchText = new String(text.getBytes(StandardCharsets.ISO_8859_1));
        noteService.update(new NoteDto(key, crutchHeading, crutchText));
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
    public void getNoteJsonRepresentation(@PathVariable("key") long key, HttpServletResponse response) {
        response.setContentType("application/json");
        try (Writer writer = new OutputStreamWriter(response.getOutputStream())) {
            writer.write(noteService.getJsonStrById(key));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @ResponseBody
    @GetMapping("/xml/{key}")
    public void getNoteXmlRepresentation(@PathVariable("key") long key, HttpServletResponse response) {
        response.setContentType("application/xml");
        try (Writer writer = new OutputStreamWriter(response.getOutputStream())) {
            writer.write(noteService.getXmlStrById(key));
        } catch (IOException e) {
            e.printStackTrace();
        }
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
