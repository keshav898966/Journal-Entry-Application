package com.example.JournalApp.repository;

import com.example.JournalApp.Entry.JournalEntry;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface JournalEntryRepository extends MongoRepository <JournalEntry, ObjectId> {

}
