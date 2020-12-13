package com.udacity.jwdnd.course1.cloudstorage.controller;

import com.udacity.jwdnd.course1.cloudstorage.model.User;
import com.udacity.jwdnd.course1.cloudstorage.model.File;
import com.udacity.jwdnd.course1.cloudstorage.services.UserService;
import com.udacity.jwdnd.course1.cloudstorage.services.FileService;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;


@Controller
public class FileController {

    private final FileService fileService;
    private final UserService userService;

    public FileController(FileService fileService, UserService appUserService) {
        this.fileService = fileService;
        this.userService = appUserService;
    }

    @PostMapping("/file-upload")
    public String postFile(@RequestParam("fileUpload") MultipartFile multipartFile, Authentication authentication,
                           RedirectAttributes redirectAttributes) {
        redirectAttributes.addFlashAttribute("activeTab", "files");
        if (multipartFile.isEmpty()) {
            redirectAttributes.addFlashAttribute("success", false);
            redirectAttributes.addFlashAttribute("error", true);
            redirectAttributes.addFlashAttribute("message", "File not selected to upload.");
            return "redirect:/result";
        }

        User user = this.userService.getUser(authentication.getName());
        Integer userId = user.getUserId();

        if (fileService.isFilenameAvailable(multipartFile.getOriginalFilename(), userId)) {

            redirectAttributes.addFlashAttribute("success", false);
            redirectAttributes.addFlashAttribute("error", true);
            redirectAttributes.addFlashAttribute("message", "File name already exists!");
            return "redirect:/result";
        }
        try {
            fileService.createFile(multipartFile, userId);
            redirectAttributes.addFlashAttribute("success", true);
            redirectAttributes.addFlashAttribute("message", "New File added successfully.");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", true);
            redirectAttributes.addFlashAttribute("message", "System error!" + e.getMessage());
        }
        return "redirect:/result";
    }

    @PostMapping("/files/delete")
    public String deleteFile(@ModelAttribute File fileDelete, Authentication authentication, RedirectAttributes redirectAttributes) {
        redirectAttributes.addFlashAttribute("activeTab", "files");
        User user = this.userService.getUser(authentication.getName());
        Integer userid = user.getUserId();

        try {
            fileService.deleteFile(fileDelete, userid);
            redirectAttributes.addFlashAttribute("success", true);
            redirectAttributes.addFlashAttribute("message", "file Deleted");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", true);
            redirectAttributes.addFlashAttribute("message", "System error!" + e.getMessage());
        }
        return "redirect:/result";
    }

    /** https://knowledge.udacity.com/questions/349439 **/
    /** https://knowledge.udacity.com/questions/348310 **/
    @GetMapping("/download/{fileId}")
    public ResponseEntity downloadFile(Authentication authentication, @PathVariable(name = "fileId") String fileIdString, RedirectAttributes redirectAttributes) throws AuthenticationException {
        redirectAttributes.addFlashAttribute("activeTab", "files");
        Integer authenticatedUserId = userService.getUser(authentication.getName()).getUserId();
        Integer fileId = Integer.parseInt(fileIdString);
        File file = fileService.getFileById(fileId);
        if (authenticatedUserId.equals(file.getUserId())) {
            return ResponseEntity.ok()
                    .contentType(MediaType.parseMediaType(file.getContentType()))
                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + file.getFilename() + "\"")
                    .body(file.getFileData());
        } else throw new AuthenticationCredentialsNotFoundException("You do not have access to this file.");
    }
}