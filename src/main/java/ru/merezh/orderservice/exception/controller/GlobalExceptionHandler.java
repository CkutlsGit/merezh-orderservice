package ru.merezh.orderservice.exception.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.MessageSourceResolvable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.method.annotation.HandlerMethodValidationException;
import ru.merezh.orderservice.exception.OrderException;
import ru.merezh.orderservice.exception.dto.ExceptionDto;
import ru.merezh.orderservice.exception.dto.ExceptionValidateDto;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    @ExceptionHandler(OrderException.class)
    public ResponseEntity<ExceptionDto> orderExceptionHandleR(OrderException e) {
        return ResponseEntity.status(e.getCode()).body(new ExceptionDto(e.getMessage()));
    }

    @ExceptionHandler(HandlerMethodValidationException.class)
    public ResponseEntity<ExceptionValidateDto> methodArgumentNotValidExceptionHandler(HandlerMethodValidationException e) {
        Map<String, List<String>> errorMessage = new HashMap<>();

        e.getParameterValidationResults().forEach(result -> {
            String paramName = result.getMethodParameter().getParameterName();
            String key = paramName != null ? paramName : "unknown";

            List<String> messages = result.getResolvableErrors().stream()
                    .map(MessageSourceResolvable::getDefaultMessage)
                    .toList();

            errorMessage
                    .computeIfAbsent(key, k -> new ArrayList<>())
                    .addAll(messages);
        });

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ExceptionValidateDto(errorMessage));
    }

    @ExceptionHandler(HttpClientErrorException.class)
    public ResponseEntity<String> httpClientErrorExceptionHandler(HttpClientErrorException e) {
        log.info("Ошибка класса клиента - {}: {}", e.getClass(), e.getMessage());

        return ResponseEntity.status(e.getStatusCode()).body(e.getResponseBodyAsString());
    }

    @ExceptionHandler(HttpServerErrorException.class)
    public ResponseEntity<String> httpServerErrorExceptionHandler(HttpServerErrorException e) {
        log.info("Ошибка класса сервера - {}: {}", e.getClass(), e.getMessage());
        return ResponseEntity.status(e.getStatusCode()).body(e.getResponseBodyAsString());
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ExceptionDto> exceptionHandler(Exception e) {
        log.info("Ошибка неизвестного класса {} - {}", e.getClass(), e.getMessage());

        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ExceptionDto("Ошибка сервиса"));
    }
}
