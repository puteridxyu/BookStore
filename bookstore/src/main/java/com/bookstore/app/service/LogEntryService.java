package com.bookstore.app.service;

import com.bookstore.app.entity.LogEntry;
import com.bookstore.app.repository.CustomerRepository;
import com.bookstore.app.repository.LogEntryRepository;

import lombok.RequiredArgsConstructor;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class LogEntryService {

    private final LogEntryRepository logEntryRepository;

    public List<LogEntry> getAll() {
        return logEntryRepository.findAll();
    }

    public LogEntry save(LogEntry entry) {
        return logEntryRepository.save(entry);
    }
}
