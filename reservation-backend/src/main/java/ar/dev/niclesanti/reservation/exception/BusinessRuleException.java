package ar.dev.niclesanti.reservation.exception;

import org.springframework.http.HttpStatus;

public class BusinessRuleException extends RuntimeException {

    private final HttpStatus httpStatus;

    public BusinessRuleException(String message) {
        this(message, HttpStatus.BAD_REQUEST);
    }

    public BusinessRuleException(String message, HttpStatus httpStatus) {
        super(message);
        this.httpStatus = httpStatus;
    }

    public HttpStatus getHttpStatus() {
        return httpStatus;
    }
}
