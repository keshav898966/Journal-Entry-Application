package com.example.JournalApp.Entry;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.NonNull;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.ArrayList;
import java.util.List;

@Document(collection = "user")
@Data
public class User {

    @Id
    private String id;

    @Indexed(unique = true)
    @NotBlank
    @Size(min = 3,max = 20)
    private String username;

    @NotBlank
    private String password;

    @DBRef
    private List<JournalEntry> journalEntryList = new ArrayList<>();

    private List<String> roles = new ArrayList<>();
}