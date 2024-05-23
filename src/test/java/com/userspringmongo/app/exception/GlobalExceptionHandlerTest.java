package com.userspringmongo.app.exception;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.context.request.WebRequest;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

public class GlobalExceptionHandlerTest {

    @InjectMocks
    private GlobalExceptionHandler globalExceptionHandler;

    @Mock
    private WebRequest webRequest;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testHandleUserException() {
        UserException userException = new UserException("User not found");

        when(webRequest.getDescription(false)).thenReturn("uri=/api/users/1");

        ResponseEntity<ErrorResponse> responseEntity = globalExceptionHandler.handleUserException(userException, webRequest);

        assertEquals(HttpStatus.NOT_FOUND.value(), responseEntity.getStatusCodeValue());
        assertEquals("User not found", responseEntity.getBody().getMessage());
        assertEquals("uri=/api/users/1", responseEntity.getBody().getDetails());
        assertEquals(LocalDateTime.now().getYear(), responseEntity.getBody().getTimestamp().getYear());
    }
}
