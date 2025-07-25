package com.example.JournalApp.repository;

import com.example.JournalApp.Entry.JournalEntry;
import com.example.JournalApp.Entry.User;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface UserRepository extends MongoRepository <User, ObjectId> {
  User findByusername(String username);

//  User deleteByUserName(String username);
}
