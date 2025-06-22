package com.bookstore.app.controller;

import com.bookstore.app.entity.LogEntry;
import com.bookstore.app.service.LogEntryService;
import lombok.RequiredArgsConstructor;

import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;

@RestController
@RequestMapping("/api/logs")
@RequiredArgsConstructor
public class LogEntryController {

    private final LogEntryService logService;

    @GetMapping
    public Flux<LogEntry> getLogs() {
        return logService.getAllLogs();
    }
}
