package com.example.controller;

import com.example.entity.Account;
import com.example.entity.Message;
import com.example.service.AccountService;
import com.example.service.MessageService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.dao.*;

import java.util.List;
import java.util.Map;
/**
 * TODO: You will need to write your own endpoints and handlers for your controller using Spring. The endpoints you will need can be
 * found in readme.md as well as the test cases. You be required to use the @GET/POST/PUT/DELETE/etc Mapping annotations
 * where applicable as well as the @ResponseBody and @PathVariable annotations. You should
 * refer to prior mini-project labs and lecture materials for guidance on how a controller may be built.
 */

@RestController
@RequestMapping("/") 
public class SocialMediaController {

    @Autowired
    private AccountService accountService;
    @Autowired
    private MessageService messageService;

    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@RequestBody Account account) {
        try {
            // Call your AccountService register method which performs validation and persistence
            Account created = accountService.register(account);
            // If successful, return the Account with 200 OK
            return ResponseEntity.ok(created);
        } catch (IllegalArgumentException e) {
            // For validation errors, return 400 Bad Request
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        } catch (DuplicateKeyException e) {
            // For username duplicate, return 409 Conflict
            return ResponseEntity.status(HttpStatus.CONFLICT).body("Username already exists.");
        } catch (Exception e) {
            // Catch-all for unexpected exceptions
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid registration data.");
        }
    }

    @PostMapping("/login")
    public ResponseEntity<?> loginUser(@RequestBody Account account) {
        Account found = accountService.login(account);
        if (found != null) {
            return ResponseEntity.ok(found);
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
    }

    @PostMapping("/messages")
    public ResponseEntity<?> createMessage(@RequestBody Message message) {
        try {
            Message created = messageService.createMessage(message);
            return ResponseEntity.ok(created);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    @DeleteMapping("/messages/{messageId}")
    public ResponseEntity<?> deleteMessage(@PathVariable int messageId) {
        int rowsDeleted = messageService.deleteMessage(messageId);

        if (rowsDeleted == 1) {
            // Return integer 1 in the body
            return ResponseEntity.ok(rowsDeleted);
        } else {
            // Return 200 OK but with an empty body
            return ResponseEntity.ok().build();
        }
    }

    @GetMapping("/accounts/{accountId}/messages")
    public ResponseEntity<List<Message>> getAllMessagesFromUser(@PathVariable int accountId) {
        List<Message> messages = messageService.getMessagesByAccountId(accountId);
        return ResponseEntity.ok(messages); // Always returns a list (empty or not)
    }

    @GetMapping("/messages")
    public ResponseEntity<List<Message>> getAllMessages() {
        List<Message> messages = messageService.getAllMessages();
        return ResponseEntity.ok(messages);
    }

    @GetMapping("/messages/{messageId}")
    public ResponseEntity<?> getMessageById(@PathVariable int messageId) {
        Message message = messageService.getMessageById(messageId);

        if (message != null) {
            // Return message as JSON
            return ResponseEntity.ok(message);
        } else {
            // Return 200 with empty body
            return ResponseEntity.ok().body("");
        }
    }

    @PatchMapping("/messages/{messageId}")
    public ResponseEntity<?> updateMessage(
            @PathVariable int messageId,
            @RequestBody Map<String, String> payload) {

        String messageText = payload.get("messageText");

        // Validate text
        if (messageText == null || messageText.trim().isEmpty() || messageText.length() > 255) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }

        int rowsUpdated = messageService.updateMessage(messageId, messageText);

        if (rowsUpdated == 0) {
            // Message not found
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }

        return ResponseEntity.ok(rowsUpdated); // Should return "1"
    }


}
