package ru.merezh.orderservice.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;

import java.math.BigDecimal;

@Schema(description = "Данные о содержимом заказе")
public record OrderItemDto(
        @NotBlank(message = "Имя позиции товара обязательна к заполнению")
        @Schema(description = "Название позиции заказа")
        String namePosition,

        @NotBlank(message = "Id позиции товара обязательна к заполнению")
        @Schema(description = "Индефикатор позиции заказа")
        String idPosition,

        @Positive(message = "Стоимость должна быть больше нуля")
        @Schema(description = "Стоимость одной позиции заказа")
        BigDecimal amount,

        @Positive(message = "Количество должно быть больше нуля")
        @Schema(description = "Количество данного товара")
        int quantity
) {
}
