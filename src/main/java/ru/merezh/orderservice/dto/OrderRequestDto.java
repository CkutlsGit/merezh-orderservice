package ru.merezh.orderservice.dto;

import java.math.BigDecimal;
import java.util.Date;

public record OrderRequestDto(
        long orderId,
        long userId,
        BigDecimal totalAmount,
        Date date
) {
}
