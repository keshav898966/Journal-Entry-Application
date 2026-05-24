package com.example.JournalApp.Controler;

import com.example.JournalApp.Entry.JournalEntry;
import com.example.JournalApp.Entry.User;
import com.example.JournalApp.Service.JournalEntryService;
import com.example.JournalApp.Service.UserService;
import com.example.JournalApp.repository.JournalEntryRepository;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;

@RestController
@RequestMapping("/journal")
public class JournalEntryControler {

    @Autowired
    private JournalEntryService journalEntryService;

    @Autowired
    private UserService userService;

    @Autowired
    private JournalEntryRepository journalEntryRepository;

    @GetMapping
    public ResponseEntity<?> getAllJournalEntriesOfUser(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int size
    ) {

        Authentication authentication =
                SecurityContextHolder.getContext().getAuthentication();

        String username = authentication.getName();

        User user = userService.findByusername(username);

        Pageable pageable = PageRequest.of(
                page,
                size,
                Sort.by("title").ascending()
        );

        int start = (int) pageable.getOffset();
        int end = Math.min((start + pageable.getPageSize()),
                user.getJournalEntryList().size());

        List<JournalEntry> pageContent =
                user.getJournalEntryList().subList(start, end);

        Page<JournalEntry> journalPage =
                new PageImpl<>(pageContent, pageable,
                        user.getJournalEntryList().size());

        return ResponseEntity.ok(journalPage);
    }

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> createEntry(

            @RequestParam String title,

            @RequestParam String content,

            @RequestPart(value = "file",
                    required = false) MultipartFile file)

            throws IOException {

        Authentication authentication =
                SecurityContextHolder.getContext().getAuthentication();

        String username = authentication.getName();

        JournalEntry myEntry = new JournalEntry();

        myEntry.setTitle(title);

        myEntry.setContent(content);

        // Optional file upload
        if (file != null && !file.isEmpty()) {

            String uploadDir = "uploads/";

            File dir = new File(uploadDir);

            if (!dir.exists()) {
                dir.mkdirs();
            }

            String filePath =
                    uploadDir + file.getOriginalFilename();

            Files.copy(
                    file.getInputStream(),
                    Paths.get(filePath),
                    StandardCopyOption.REPLACE_EXISTING
            );

            myEntry.setImagePath(filePath);
        }

        journalEntryService.saveEntry(myEntry, username);

        return ResponseEntity.ok(myEntry);
    }

    @GetMapping("/id/{id}")
    public ResponseEntity<?> getById(@PathVariable String id){

        return ResponseEntity.ok(
                journalEntryService.findById(id)
        );
    }

    @DeleteMapping("/id/{id}")
    public ResponseEntity<?> deleteById(@PathVariable String id){

        Authentication authentication =
                SecurityContextHolder.getContext().getAuthentication();

        String username = authentication.getName();

        journalEntryService.deleteById(id, username);

        return ResponseEntity.ok("Deleted");
    }
}