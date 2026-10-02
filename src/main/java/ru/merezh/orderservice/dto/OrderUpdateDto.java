package ru.merezh.orderservice.dto;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Данные для обновления статуса заказа")
public record OrderUpdateDto(

        @Schema(description = "Индефикатор заказа")
        long orderId,

        @Schema(description = "Статус заказа (Приходит с Payment)")
        String status
) {
}
