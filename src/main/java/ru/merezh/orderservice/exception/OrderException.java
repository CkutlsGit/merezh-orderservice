package ru.merezh.orderservice.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class OrderException extends RuntimeException {

    private HttpStatus code;

    public OrderException(String message) {
        super(message);
        this.code = HttpStatus.BAD_REQUEST;
    }

    public OrderException(String message, HttpStatus code) {
        super(message);
        this.code = code;
    }
}
