package com.bookstore.app.service;

import com.bookstore.app.entity.LogEntry;
import com.bookstore.app.repository.LogEntryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;

@Service
@RequiredArgsConstructor
public class LogEntryService {

    private final LogEntryRepository logRepository;

    public Flux<LogEntry> getAllLogs() {
        return logRepository.findAll();
    }
}