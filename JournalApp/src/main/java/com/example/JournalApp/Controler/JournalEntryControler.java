package com.example.JournalApp.Controler;

import com.example.JournalApp.Entry.JournalEntry;
import com.example.JournalApp.Entry.User;
import com.example.JournalApp.Service.JournalEntryService;
import com.example.JournalApp.Service.UserService;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/journal")
public class JournalEntryControler {

    @Autowired
    private JournalEntryService journalEntryService;

    @Autowired
    private UserService userService;


    @GetMapping("{username}")
   public ResponseEntity<?> getAllUserJournalEntry(@PathVariable String username){
        User user = userService.findByusername(username);
        List<JournalEntry> all= user.getJournalEntryList();
       if(all!=null && !all.isEmpty()){
           return new ResponseEntity<>(all, HttpStatus.OK);
       }
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
   }



    @PostMapping("{username}")
   public ResponseEntity<JournalEntry> createEntry(@RequestBody JournalEntry myEntry,@PathVariable String username){
        try{
            myEntry.setDate(LocalDateTime.now());
            journalEntryService.saveEntry(myEntry,username);
            return new ResponseEntity<>(HttpStatus.CREATED);
        }catch (Exception e){
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

   }

   @GetMapping("id/{myid}")
    public ResponseEntity<JournalEntry> getbyId(@PathVariable ObjectId myid){

       Optional<JournalEntry> journalEntry = journalEntryService.findById(myid);
       if(journalEntry.isPresent()){
           return new ResponseEntity<>(journalEntry.get(), HttpStatus.OK);
       }
       return new ResponseEntity<>(HttpStatus.NOT_FOUND);
   }

    @DeleteMapping("id/{username}/{myid}")
    public ResponseEntity<JournalEntry> deletbyId(@PathVariable ObjectId myid,@PathVariable String username){
        journalEntryService.deleteById(myid,username);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @DeleteMapping
    public ResponseEntity<JournalEntry> delete(){
        journalEntryService.delete();
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }


    @PutMapping("id/{username}/{id}")
    public ResponseEntity<?> updatebyid(@PathVariable ObjectId id,
                                        @RequestBody JournalEntry newEn,
                                        @PathVariable String username){
        JournalEntry old= journalEntryService.findById(id).orElse(null);
        if(old!=null) {
           old.setTitle(newEn.getTitle()!=null && !newEn.getTitle().equals("")?newEn.getTitle():old.getTitle());
           old.setContent(newEn.getContent()!=null && !newEn.getContent().equals("")?newEn.getContent():old.getContent());

            journalEntryService.saveEntry(old);
            return new ResponseEntity<>(old,HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);

    }

}
