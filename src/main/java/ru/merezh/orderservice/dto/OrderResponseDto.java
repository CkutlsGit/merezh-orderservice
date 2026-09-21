package ru.merezh.orderservice.dto;

import ru.merezh.orderservice.entity.enums.OrderStatus;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

public record OrderResponseDto(
        long orderId,
        long userId,
        List<OrderItemDto> items,
        BigDecimal totalAmount,
        OrderStatus status,
        Date date
) {
}
