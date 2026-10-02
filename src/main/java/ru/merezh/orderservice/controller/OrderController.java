package ru.merezh.orderservice.controller;

import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.merezh.orderservice.dto.OrderDto;
import ru.merezh.orderservice.dto.OrderItemDto;
import ru.merezh.orderservice.dto.OrderResponseDto;
import ru.merezh.orderservice.dto.OrderUpdateDto;
import ru.merezh.orderservice.entity.enums.OrderStatus;
import ru.merezh.orderservice.service.OrderService;

import java.util.List;

@RestController
@RequestMapping("/api/v1/orders")
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;

    @GetMapping("/get/{id}")
    @Operation(summary = "Метод получение заказа по id")
    public ResponseEntity<OrderResponseDto> getOrder(@PathVariable long id) {
        return ResponseEntity.ok().body(orderService.getOrder(id));
    }

    @GetMapping("/get/user")
    @Operation(summary = "Метод получение истории заказов пользователя по его id")
    public ResponseEntity<List<OrderResponseDto>> getOrdersByUserId(@RequestHeader("X-User-Id") long userId) {
        return ResponseEntity.ok().body(orderService.getOrdersByUserId(userId));
    }

    @PostMapping("/create")
    @Operation(summary = "Метод для создания объекта заказа")
    public ResponseEntity<OrderStatus> createOrder(@RequestHeader("X-User-Id") long userId, @RequestBody @Valid List<OrderItemDto> items) {
        return ResponseEntity.ok().body(orderService.createOrder(new OrderDto(userId, items)));
    }

    @PostMapping("/update")
    @Operation(summary = "Метод для обновления статуса заказа по его id")
    public ResponseEntity<OrderStatus> changeStatus(@RequestBody OrderUpdateDto orderUpdateDto) {
        return ResponseEntity.ok().body(orderService.changeStatus(orderUpdateDto));
    }
}
