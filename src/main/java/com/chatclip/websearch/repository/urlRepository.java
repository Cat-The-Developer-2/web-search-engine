package com.chatclip.websearch.repository;

import com.chatclip.websearch.models.Models;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface urlRepository extends MongoRepository<Models, String> {
    Optional<Models> findByUrl(String url);
}