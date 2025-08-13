package com.example.repository;

import com.example.entity.Message;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface MessageRepository extends JpaRepository<Message, Integer> {
    // For now, no extra query methods needed for create
    @Query("SELECT COUNT(a) > 0 FROM Account a WHERE a.accountId = :postedBy")
    boolean existsByPostedBy(@Param("postedBy") int postedBy);
}