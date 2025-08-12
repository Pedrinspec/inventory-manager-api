package com.maninv.inventory_manager_api.domain.exception;

import java.time.LocalDateTime;

public record ErrorResponse(LocalDateTime timestamp, int status, String message) {
}
