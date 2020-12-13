package com.udacity.jwdnd.course1.cloudstorage.controller;

import com.udacity.jwdnd.course1.cloudstorage.model.User;
import com.udacity.jwdnd.course1.cloudstorage.model.Note;
import com.udacity.jwdnd.course1.cloudstorage.services.NoteService;
import com.udacity.jwdnd.course1.cloudstorage.services.UserService;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class NoteController {
    private final NoteService noteService;
    private final UserService userService;

    public NoteController(NoteService noteService, UserService userService) {
        this.noteService = noteService;
        this.userService = userService;
    }

    @PostMapping("/notes")
    public String postNote(Authentication authentication, RedirectAttributes redirectAttributes, @ModelAttribute Note note) {
        redirectAttributes.addFlashAttribute("activeTab", "notes");
        User user = this.userService.getUser(authentication.getName());
        Integer userid = user.getUserId();
        note.setUserId(userid);

        try {
            if (note.getNoteId() == null) {
                noteService.createNote(note);
            } else {
                noteService.editNote(note);
            }
            redirectAttributes.addFlashAttribute("success", true);
            redirectAttributes.addFlashAttribute("message", "New note added !");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", true);
            redirectAttributes.addFlashAttribute("message", "System error!" + e.getMessage());
        }
        return "redirect:/result";
    }

    @PostMapping("/notes/delete")
    public String deleteNote(RedirectAttributes redirectAttributes, @ModelAttribute Note note) {
        redirectAttributes.addFlashAttribute("activeTab", "notes");
        Integer noteId = note.getNoteId();

        try {
            noteService.deleteNote(noteId);
            redirectAttributes.addFlashAttribute("success", true);
            redirectAttributes.addFlashAttribute("message", "Note deleted!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", true);
            redirectAttributes.addFlashAttribute("message", "System error!" + e.getMessage());
        }
        return "redirect:/result";
    }
}
