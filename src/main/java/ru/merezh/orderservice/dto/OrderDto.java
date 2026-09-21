package ru.merezh.orderservice.dto;

import java.util.List;

public record OrderDto(
        long userId,
        List<OrderItemDto> items
) {
}
