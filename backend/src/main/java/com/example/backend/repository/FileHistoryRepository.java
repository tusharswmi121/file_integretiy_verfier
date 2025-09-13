package com.example.backend.repository;

import com.example.backend.model.FileHistory;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FileHistoryRepository extends JpaRepository<FileHistory, Long> {
}