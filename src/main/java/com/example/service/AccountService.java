package com.example.service;

import com.example.entity.Account;
import com.example.repository.AccountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class AccountService {

    @Autowired
    private AccountRepository accountRepository;

    // Register new account
    public Account register(Account account) {
        // Validate username
        if (account.getUsername() == null || account.getUsername().trim().isEmpty()) {
            throw new IllegalArgumentException("Username cannot be blank");
        }
        // Validate password length
        if (account.getPassword() == null || account.getPassword().length() < 4) {
            throw new IllegalArgumentException("Password must be at least 4 characters");
        }
        // Check for duplicate username
        Optional<Account> existing = accountRepository.findByUsername(account.getUsername());
        if (existing.isPresent()) {
            throw new DuplicateKeyException("Username already exists");
        }

        // Save account
        return accountRepository.save(account);
    }

    // Login
    public Account login(Account account) {
        Optional<Account> existing = accountRepository.findByUsernameAndPassword(
                account.getUsername(), account.getPassword());

        return existing.orElse(null); // Controller will handle 401 if null
    }
}
