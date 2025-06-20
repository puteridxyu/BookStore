package com.bookstore.app.controller;

import com.bookstore.app.entity.LogEntry;
import com.bookstore.app.service.LogEntryService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/logs")
@RequiredArgsConstructor
public class LogEntryController {

    private final LogEntryService logEntryService;

    @GetMapping
    public List<LogEntry> getAll() {
        return logEntryService.getAll();
    }

    @PostMapping
    public LogEntry create(@RequestBody LogEntry log) {
        return logEntryService.save(log);
    }
}
