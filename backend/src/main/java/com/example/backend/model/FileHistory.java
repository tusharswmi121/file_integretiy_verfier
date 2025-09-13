package com.example.backend.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
public class FileHistory {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String filename;
    private String status; // "uploaded", "verified", "tampered"
    private LocalDateTime timestamp;

    public FileHistory() {}

    public FileHistory(String filename, String status, LocalDateTime timestamp) {
        this.filename = filename;
        this.status = status;
        this.timestamp = timestamp;
    }

    // Getters and setters...
}