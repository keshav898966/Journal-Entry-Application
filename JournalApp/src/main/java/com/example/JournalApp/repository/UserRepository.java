package com.example.JournalApp.repository;

import com.example.JournalApp.Entry.User;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface UserRepository extends MongoRepository<User, String> {

  User findByusername(String username);

  void deleteByusername(String username);
}