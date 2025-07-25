package com.example.JournalApp.Controler;

import com.example.JournalApp.Entry.User;
import com.example.JournalApp.Service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/public")
public class PublicControler {

    @Autowired
    private UserService userService;

    @GetMapping("/healthCheck")
    public String helthCHeck(){
        return "okk";
    }

    @PostMapping("/createu")
    public ResponseEntity<User> saveEntry(@RequestBody User user){
        userService.save(user);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
