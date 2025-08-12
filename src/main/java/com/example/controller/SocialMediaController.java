package com.example.controller;

import com.example.entity.Account;
import com.example.service.AccountService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
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

}
