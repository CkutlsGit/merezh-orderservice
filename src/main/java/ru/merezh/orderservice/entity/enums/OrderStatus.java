package ru.merezh.orderservice.entity.enums;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Данные о статусе оплаты")
public enum OrderStatus {
    PAYMENT_SUCCESS,
    PAYMENT_FAILED,
    PAYMENT_WAITING
}
