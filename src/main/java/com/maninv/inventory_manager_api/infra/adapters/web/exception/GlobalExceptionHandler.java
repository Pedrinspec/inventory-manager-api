package com.maninv.inventory_manager_api.infra.adapters.web.exception;

import com.maninv.inventory_manager_api.domain.exception.BusinessException;
import com.maninv.inventory_manager_api.domain.exception.ErrorResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(BusinessException.class)
    public ResponseEntity<String> handleDomainException(BusinessException ex) {
        if (ex.getMessage().contains("não encontrado")) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
        }
        return ResponseEntity.badRequest().body(ex.getMessage());
    }

    @ExceptionHandler({IllegalArgumentException.class, RuntimeException.class})
    public ResponseEntity<ErrorResponse> handleNotFound(RuntimeException ex) {
        log.error("Erro ao obter dados para requisição: {}", ex.getMessage());
        ErrorResponse error = new ErrorResponse(LocalDateTime.now(), HttpStatus.NOT_FOUND.value(), ex.getMessage());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleGlobalException(Exception ex) {
        log.error("Erro inesperado: {}", ex.getMessage());
        ErrorResponse error = new ErrorResponse(LocalDateTime.now(), HttpStatus.NOT_FOUND.value(), ex.getMessage());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
    }
}
