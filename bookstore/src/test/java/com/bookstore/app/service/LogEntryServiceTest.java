package com.bookstore.app.service;

import com.bookstore.app.entity.LogEntry;
import com.bookstore.app.repository.LogEntryRepository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;

import java.time.LocalDateTime;

import static org.mockito.Mockito.*;

class LogEntryServiceTest {

    @Mock
    private LogEntryRepository repository;

    @InjectMocks
    private LogEntryService service;

    private LogEntry sampleLog;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        sampleLog = new LogEntry();
        sampleLog.setLogId(1L);
        sampleLog.setTimestamp(LocalDateTime.now());
        sampleLog.setMethod("POST");
        sampleLog.setEndpoint("/api/customers");
        sampleLog.setRequestBody("{\"name\":\"Puteri\"}");
        sampleLog.setResponseBody("{\"id\":1,\"name\":\"Puteri\"}");
        sampleLog.setStatusCode(201);
        sampleLog.setIpAddress("127.0.0.1");
        sampleLog.setUserId(10L);
    }

    @Test
    void testGetAllLogs() {
        when(repository.findAll()).thenReturn(Flux.just(sampleLog));

        StepVerifier.create(service.getAllLogs())
                .expectNextMatches(log ->
                        log.getLogId() == 1L &&
                        "POST".equals(log.getMethod()) &&
                        "/api/customers".equals(log.getEndpoint()) &&
                        log.getStatusCode() == 201 &&
                        log.getUserId() == 10L
                )
                .verifyComplete();

        verify(repository, times(1)).findAll();
    }
}
