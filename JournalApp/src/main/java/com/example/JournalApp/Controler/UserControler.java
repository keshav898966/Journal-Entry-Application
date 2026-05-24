package com.example.JournalApp.Controler;

import com.example.JournalApp.Entry.JournalEntry;
import com.example.JournalApp.Entry.User;
import com.example.JournalApp.Service.JournalEntryService;
import com.example.JournalApp.Service.UserService;
import com.example.JournalApp.dto.PasswordUpdateRequest;
import com.example.JournalApp.dto.UsernameUpdateRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;

@RestController
@RequestMapping("/user")
public class UserControler {

    @Autowired
    private UserService userService;

    @Autowired
    private JournalEntryService journalEntryService;

    @PatchMapping("/username")
    public ResponseEntity<?> updateUsername(
            @RequestBody UsernameUpdateRequest request,
            HttpServletResponse response){

        try {

            userService.updateUsername(
                    request,
                    response
            );

            return ResponseEntity.ok(
                    "Username updated successfully"
            );

        } catch (Exception e){

            return ResponseEntity.badRequest()
                    .body(e.getMessage());
        }
    }

    @PatchMapping("/password")
    public ResponseEntity<?> updatePassword(
            @RequestBody PasswordUpdateRequest request,
            HttpServletResponse response){

        try {

            userService.updatePassword(
                    request,
                    response
            );

            return ResponseEntity.ok(
                    "Password updated successfully"
            );

        } catch (Exception e){

            return ResponseEntity.badRequest()
                    .body(e.getMessage());
        }
    }
    @DeleteMapping
    public ResponseEntity<?> deleteUser(){

        String username = SecurityContextHolder
                .getContext()
                .getAuthentication()
                .getName();

        User user = userService.findByusername(username);

        if(user == null){
            return ResponseEntity.badRequest()
                    .body("User not found");
        }

        // Delete all journal entries
        for(JournalEntry entry : user.getJournalEntryList()){

            journalEntryService.deleteById(entry.getId());
        }

        // Delete user
        userService.deleteByUsername(username);

        return ResponseEntity.ok("User and all journal entries deleted");
    }
}