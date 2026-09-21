package ru.merezh.orderservice.dto;

public record OrderUpdateDto(
        long orderId,
        String status
) {
}
