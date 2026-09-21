package ru.merezh.orderservice.exception.dto;

import java.util.List;
import java.util.Map;

public record ExceptionValidateDto(
        Map<String, List<String>> errorMessage
) {
}