package com.example.backend.service;

import com.example.backend.model.FileRecord;
import com.example.backend.repository.FileRecordRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.security.MessageDigest;
import java.time.LocalDateTime;
import java.util.Optional;

@Service
public class FileService {

    @Autowired
    private FileRecordRepository repo;

    // Save file's hash during upload
    public void saveFile(String filename, byte[] fileContent) {
        String hash = computeHash(fileContent);

        FileRecord record = new FileRecord();
        record.setFilename(filename);
        record.setHash(hash);
        record.setUploadedAt(LocalDateTime.now());

        // When uploading, we clear previous verification status
        record.setLastStatus("NOT_VERIFIED");
        record.setLastVerifiedAt(null);

        repo.save(record);
    }

    // Verify file's hash and update status
    public String verifyFile(String filename, byte[] fileContent) {
        Optional<FileRecord> existing = repo.findByFilename(filename);
        String incomingHash = computeHash(fileContent);

        // If not found
        if (existing.isEmpty()) {
            return "NOT_FOUND";
        }

        FileRecord record = existing.get();
        record.setLastVerifiedAt(LocalDateTime.now()); // Log current check time

        if (incomingHash.equals(record.getHash())) {
            record.setLastStatus("MATCH");
            repo.save(record);
            return "MATCH";
        } else {
            record.setLastStatus("TAMPERED");
            repo.save(record);
            return "MISMATCH";
        }
    }

    // Compute SHA-256 hash
    private String computeHash(byte[] content) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hashed = digest.digest(content);
            StringBuilder sb = new StringBuilder();
            for (byte b : hashed) {
                sb.append(String.format("%02x", b));
            }
            return sb.toString();
        } catch (Exception e) {
            throw new RuntimeException("Hashing failed", e);
        }
    }
}
