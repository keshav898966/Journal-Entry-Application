package com.example.JournalApp.Controler;

import com.example.JournalApp.Entry.JournalEntry;
import com.example.JournalApp.Entry.User;
import com.example.JournalApp.Service.JournalEntryService;
import com.example.JournalApp.Service.UserService;
import com.example.JournalApp.repository.UserRepository;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.parameters.P;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/user")
public class UserControler {

    @Autowired
    private UserService userService;

    @Autowired
    private UserRepository userRepository;




    @PutMapping
    public ResponseEntity<?> update(@RequestBody User user){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String name = authentication.getName();
        User userinDb = userService.findByusername(name);
            userinDb.setUsername(user.getUsername());
            userinDb.setPassword(user.getPassword());
            userService.save(userinDb);
        return new ResponseEntity<>(HttpStatus.OK);
    }

//    @DeleteMapping
//    public ResponseEntity<?> deletUserById(){
//        Authentication authentication=SecurityContextHolder.getContext().getAuthentication();
//        userRepository.deleteByUserName(authentication.getName());
//        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
//    }

}
