package ru.merezh.orderservice.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import ru.merezh.orderservice.entity.enums.OrderStatus;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

@Schema(description = "Данные о заказе")
public record OrderResponseDto(

        @Schema(description = "Индефикатор заказа")
        long orderId,

        @Schema(description = "Индефикатор пользователя")
        long userId,

        List<OrderItemDto> items,

        @Schema(description = "Общая сумма оплаты заказа")
        BigDecimal totalAmount,

        OrderStatus status,

        @Schema(description = "Дата заказа")
        Date date
) {
}
