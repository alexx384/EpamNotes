package org.palenyy.controller;

import org.palenyy.dto.BookDto;
import org.palenyy.entity.Book;
import org.palenyy.NoteStorage;
import org.palenyy.dao.BookDao;
import org.palenyy.service.BookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/note")
public class NoteController {
    @Autowired
    private BookService bookService;

    @Autowired
    private NoteStorage noteStorage;

    @GetMapping("/h2")
    @ResponseBody
    public String h2Database() {
        StringBuilder builder = new StringBuilder("The books: ");
        for (BookDto book : bookService.findAll()) {
            builder.append(book.toString());
            builder.append('\n');
        }
        return builder.toString();
    }

//    @GetMapping("/h2/{bookName}")
//    @ResponseBody
//    public String h2DatabaseSave(@PathVariable("key") String bookName) {
//        if (bookName.isEmpty()) {
//            return "Fail";
//        }
//        repository.save(new Book(bookName));
//        return "OK";
//    }

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
