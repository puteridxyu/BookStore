package com.bookstore.app.repository;

import com.bookstore.app.entity.LogEntry;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LogEntryRepository extends ReactiveCrudRepository<LogEntry, Long> {}