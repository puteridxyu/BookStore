package com.bookstore.app.service;

import com.bookstore.app.entity.LogEntry;
import com.bookstore.app.repository.LogEntryRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.stereotype.Service;

import reactor.core.publisher.Flux;

@Slf4j
@Service
@RequiredArgsConstructor
public class LogEntryService {

    private final LogEntryRepository logRepository;

    public Flux<LogEntry> getAllLogs() {
    	log.info("Fetching all logs from DB");
        return logRepository.findAll();
    }
}