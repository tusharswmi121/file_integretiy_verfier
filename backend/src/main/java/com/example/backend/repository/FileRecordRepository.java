package com.example.backend.repository;

import com.example.backend.model.FileRecord;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface FileRecordRepository extends JpaRepository<FileRecord, Long> {
    Optional<FileRecord> findByFilename(String filename);
}
