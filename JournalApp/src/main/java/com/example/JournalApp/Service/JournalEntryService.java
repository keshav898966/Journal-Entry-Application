package com.example.JournalApp.Service;

import com.example.JournalApp.Entry.JournalEntry;
import com.example.JournalApp.Entry.User;
import com.example.JournalApp.repository.JournalEntryRepository;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Component
public class JournalEntryService {
    @Autowired
    private JournalEntryRepository journalEntryRepository;

    @Autowired
    private UserService userService;

    @Transactional
    public void saveEntry(JournalEntry journalEntry, String username){
        try {
            User user = userService.findByusername(username);
            JournalEntry saved = journalEntryRepository.save(journalEntry);
            user.getJournalEntryList().add(saved);
            //user.setUsername(null);
            userService.save(user);
        }catch (Exception e){
            System.out.println(e);
        }

    }

    public void saveEntry(JournalEntry journalEntry){
        journalEntryRepository.save(journalEntry);
    }

    public List<JournalEntry> getAll(){
        return journalEntryRepository.findAll();
    }

    public Optional<JournalEntry> findById(ObjectId id){
        return journalEntryRepository.findById(id);
    }

    public boolean delete(){
        journalEntryRepository.deleteAll();
        return true;
    }

    public boolean deleteById(ObjectId id, String username){
        User user = userService.findByusername(username);
        user.getJournalEntryList().removeIf(x ->x.getId().equals(id));
        userService.save(user);
        journalEntryRepository.deleteById(id);
        return true;
    }




}
