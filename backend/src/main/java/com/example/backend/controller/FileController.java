package com.example.backend.controller;

import com.example.backend.model.FileRecord;
import com.example.backend.repository.FileRecordRepository;
import com.example.backend.service.FileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@CrossOrigin
@RequestMapping("/api/files")
public class FileController {

    @Autowired
    private FileService fileService;

    @Autowired
    private FileRecordRepository repo;

    @PostMapping("/upload")
    public ResponseEntity<String> upload(@RequestParam("file") MultipartFile file,
                                         @RequestParam("filename") String filename) {
        try {
            fileService.saveFile(filename, file.getBytes());
            return ResponseEntity.ok("✅ File uploaded successfully and hash saved.");
        } catch (Exception e) {
            return ResponseEntity.status(500).body("❌ Upload failed due to server error.");
        }
    }

    @PostMapping("/verify")
    public ResponseEntity<String> verify(@RequestParam("file") MultipartFile file,
                                        @RequestParam("filename") String filename) {
        try {
            String result = fileService.verifyFile(filename, file.getBytes());

            switch (result) {
                case "MATCH":
                    return ResponseEntity.ok("✅ File matches original record.");
                case "MISMATCH":
                    return ResponseEntity.status(409).body("❌ File is tampered! Contents are different.");
                case "NOT_FOUND":
                    return ResponseEntity.status(404).body("📂 This file was never uploaded before.");
                default:
                    return ResponseEntity.status(500).body("⚠️ Unknown verification result.");
            }

        } catch (Exception e) {
            return ResponseEntity.status(500).body("🚨 Verification failed due to server error.");
        }
    }

    // ✅ New endpoint for verification history
    @GetMapping("/history")
    public List<FileRecord> getFileHistory() {
        return repo.findAll();
    }
}
