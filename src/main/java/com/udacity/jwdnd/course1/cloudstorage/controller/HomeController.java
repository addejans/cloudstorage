package com.udacity.jwdnd.course1.cloudstorage.controller;

import com.udacity.jwdnd.course1.cloudstorage.model.User;
import com.udacity.jwdnd.course1.cloudstorage.services.*;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/home")
public class HomeController {

    private final UserService userService;
    private final FileService fileService;
    private final NoteService noteService;
    private final CredentialService credentialService;
    private final EncryptionService encryptionService;

    public HomeController(UserService userService, FileService fileService, NoteService noteService, CredentialService credentialService, EncryptionService encryptionService) {
        this.userService = userService;
        this.fileService = fileService;
        this.noteService = noteService;
        this.credentialService = credentialService;
        this.encryptionService = encryptionService;
    }

    @GetMapping
    public String getHomePage(Authentication authentication, Model model) {
        model.addAttribute("greetingMessage", "Welcome back, " + userService.getUser(authentication.getName()).getFirstName() + "!");
        User user = this.userService.getUser(authentication.getName());
        Integer userId = user.getUserId();
        model.addAttribute("encryptionService", encryptionService);
        model.addAttribute("credentials", this.credentialService.getCredentialByUserId(userId));
        model.addAttribute("notes", this.noteService.getAllNotesByUserId(userId));
        model.addAttribute("files", this.fileService.getAllFilesByUserId(userId));
        model.addAttribute("activeTab", "files");
        return "home";
    }

}

