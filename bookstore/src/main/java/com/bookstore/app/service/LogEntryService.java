package com.bookstore.app.service;

import com.bookstore.app.entity.LogEntry;
import com.bookstore.app.repository.LogEntryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class LogEntryService {

    @Autowired
    private LogEntryRepository logEntryRepository;

    public List<LogEntry> getAll() {
        return logEntryRepository.findAll();
    }

    public LogEntry save(LogEntry entry) {
        return logEntryRepository.save(entry);
    }
}
