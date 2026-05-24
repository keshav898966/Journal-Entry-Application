package com.example.JournalApp.Entry;

import lombok.Data;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Document(collection = "journalE")
@Data
public class JournalEntry {

    @Id
    private String id;

    private String title;

    private String content;
    private String imagePath;

    private LocalDateTime date;
}