package ru.merezh.orderservice.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;

import java.math.BigDecimal;

public record OrderItemDto(
        @NotBlank(message = "Имя позиции товара обязательна к заполнению")
        String namePosition,

        @NotBlank(message = "Id позиции товара обязательна к заполнению")
        String idPosition,

        @Positive(message = "Стоимость должна быть больше нуля")
        BigDecimal amount,

        @Positive(message = "Количество должно быть больше нуля")
        int quantity
) {
}
