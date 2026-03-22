package ar.dev.niclesanti.reservation.exception;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(BusinessRuleException.class)
    public ResponseEntity<ErrorBody> handleBusinessRule(BusinessRuleException ex) {
        return ResponseEntity.status(ex.getHttpStatus()).body(new ErrorBody(ex.getMessage()));
    }

    public record ErrorBody(String message) {}
}
