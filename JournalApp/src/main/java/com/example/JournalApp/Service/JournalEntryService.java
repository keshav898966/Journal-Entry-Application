package com.example.JournalApp.Service;

import com.example.JournalApp.Entry.JournalEntry;
import com.example.JournalApp.Entry.User;
import com.example.JournalApp.repository.JournalEntryRepository;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class JournalEntryService {

    @Autowired
    private JournalEntryRepository journalEntryRepository;

    @Autowired
    private UserService userService;

//    @Transactional
    public void saveEntry(JournalEntry journalEntry, String username){

        User user = userService.findByusername(username);

        if(user == null){
            throw new RuntimeException("User not found");
        }

        journalEntry.setDate(LocalDateTime.now());

        JournalEntry saved = journalEntryRepository.save(journalEntry);

        user.getJournalEntryList().add(saved);

        userService.saveUser(user);
    }

    public void saveEntry(JournalEntry journalEntry){
        journalEntryRepository.save(journalEntry);
    }

    public List<JournalEntry> getAll(){
        return journalEntryRepository.findAll();
    }

    public Optional<JournalEntry> findById(String id){
        return journalEntryRepository.findById(id);
    }

    public void deleteAll(){
        journalEntryRepository.deleteAll();
    }

    public void deleteById(String id, String username){

        User user = userService.findByusername(username);

        user.getJournalEntryList().removeIf(x -> x.getId().equals(id));

        userService.saveUser(user);

        journalEntryRepository.deleteById(id);
    }
    public void deleteById(String id){
        journalEntryRepository.deleteById(id);
    }
}