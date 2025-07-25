package com.example.JournalApp.Service;

import com.example.JournalApp.Entry.JournalEntry;
import com.example.JournalApp.Entry.User;
import com.example.JournalApp.repository.JournalEntryRepository;
import com.example.JournalApp.repository.UserRepository;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestBody;

import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@Component
public class UserService {
    @Autowired
    private UserRepository userRepository;

    private static final PasswordEncoder passwordEncoder=new BCryptPasswordEncoder();


    public void save(User user){
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setRoles(Arrays.asList("User"));
        userRepository.save(user);
    }

//    public void savenext(User user){
//        userRepository.save(user);
//    }



    public List<User> getAll(){
        return userRepository.findAll();
    }

    public User findByusername(String username){
       return userRepository.findByusername(username);
    }



}
