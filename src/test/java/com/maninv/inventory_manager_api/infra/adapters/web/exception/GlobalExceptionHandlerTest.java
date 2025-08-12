package com.maninv.inventory_manager_api.infra.adapters.web.exception;

import com.maninv.inventory_manager_api.domain.exception.BusinessException;
import com.maninv.inventory_manager_api.domain.exception.ErrorResponse;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@ExtendWith(MockitoExtension.class)
class GlobalExceptionHandlerTest {

    @InjectMocks
    private GlobalExceptionHandler globalExceptionHandler;

    @Test
    void handleDomainException_shouldReturnNotFound_whenMessageContainsNotFound() {
        var exception = new BusinessException("Item not found");

        ResponseEntity<ErrorResponse> response = globalExceptionHandler.handleDomainException(exception);

        assertNotNull(response);
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(404, response.getBody().status());
        assertEquals("Item not found", response.getBody().message());
    }

    @Test
    void handleDomainException_shouldReturnBadRequest_forOtherBusinessExceptions() {
        var exception = new BusinessException("Invalid input");

        ResponseEntity<ErrorResponse> response = globalExceptionHandler.handleDomainException(exception);

        assertNotNull(response);
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(400, response.getBody().status());
        assertEquals("Invalid input", response.getBody().message());
    }

    @Test
    void handleGlobalException_shouldReturnInternalServerError() {
        var exception = new RuntimeException("Unexpected error");

        ResponseEntity<ErrorResponse> response = globalExceptionHandler.handleGlobalException(exception);

        assertNotNull(response);
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(500, response.getBody().status());
        assertEquals("Unexpected error", response.getBody().message());
    }
}