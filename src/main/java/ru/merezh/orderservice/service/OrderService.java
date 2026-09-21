package ru.merezh.orderservice.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;
import ru.merezh.orderservice.dto.*;
import ru.merezh.orderservice.entity.OrderItem;
import ru.merezh.orderservice.entity.Order;
import ru.merezh.orderservice.entity.enums.OrderStatus;
import ru.merezh.orderservice.exception.OrderException;
import ru.merezh.orderservice.repository.OrderRepository;

import java.util.Date;
import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class OrderService {

    private final OrderRepository orderRepository;
    private final RestTemplate restTemplate;

    @Value("${service.payments.url}")
    private String basePaymentUrl;

    @Transactional
    public OrderStatus createOrder(OrderDto orderDto) {
        Order order = new Order();
        order.setUserId(orderDto.userId());
        order.setOrderStatus(OrderStatus.PAYMENT_WAITING);
        order.setDate(new Date());

        List<OrderItem> items = orderDto.items().stream()
                .map(dto -> {
                    OrderItem item = new OrderItem(
                            dto.namePosition(),
                            dto.idPosition(),
                            dto.amount(),
                            dto.quantity()
                    );
                    item.setOrder(order);
                    return item;
                })
                .toList();
        order.setItems(items);
        order.calculateTotalAmount();

        orderRepository.save(order);

        String response = sendRequest(new OrderRequestDto(
                order.getOrderId(),
                order.getUserId(),
                order.getTotalAmount(),
                order.getDate()
        ));

        log.info("Answer from send request response - {}", response);
        return order.getOrderStatus();
    }

    @Transactional
    public OrderStatus changeStatus(OrderUpdateDto orderUpdateDto) {
        Order order = orderRepository.getOrderByOrderId(orderUpdateDto.orderId())
                .orElseThrow(() -> new OrderException("Заказ с указанным id не найден", HttpStatus.NOT_FOUND));

        switch (orderUpdateDto.status()) {
            case "SUCCESS":
                order.setOrderStatus(OrderStatus.PAYMENT_SUCCESS);
                break;
            case "FAILED":
                order.setOrderStatus(OrderStatus.PAYMENT_FAILED);
                break;
            case "WAITING":
                order.setOrderStatus(OrderStatus.PAYMENT_WAITING);
                break;
            default:
                throw new OrderException("Неверно указан статус");
        }

        return order.getOrderStatus();
    }

    @Transactional(readOnly = true)
    public OrderResponseDto getOrder(long orderId) {
        Order order =  orderRepository.getOrderByOrderId(orderId)
                .orElseThrow(() -> new OrderException("Заказ не найден", HttpStatus.NOT_FOUND));

        List<OrderItemDto> items = order.getItems().stream()
                .map(item -> new OrderItemDto(
                        item.getNamePosition(),
                        item.getIdPosition(),
                        item.getAmount(),
                        item.getQuantity()
                ))
                .toList();

        return new OrderResponseDto(
                order.getOrderId(),
                order.getUserId(),
                items,
                order.getTotalAmount(),
                order.getOrderStatus(),
                order.getDate()
        );
    }

    @Transactional(readOnly = true)
    public List<OrderResponseDto> getOrdersByUserId(long userId) {
        List<Order> orders = orderRepository.getOrdersByUserId(userId);

        return orders.stream()
                .map(order -> new OrderResponseDto(
                        order.getOrderId(),
                        order.getUserId(),
                        order.getItems().stream()
                                .map(item -> new OrderItemDto(
                                        item.getNamePosition(),
                                        item.getIdPosition(),
                                        item.getAmount(),
                                        item.getQuantity()
                                ))
                                .toList(),
                        order.getTotalAmount(),
                        order.getOrderStatus(),
                        order.getDate()
                ))
                .toList();
    }

    private String sendRequest(OrderRequestDto orderRequestDto) {
        String response = restTemplate.postForObject(
                basePaymentUrl + "/place",
                orderRequestDto,
                String.class
        );

        log.info("Response send request-  {}", response);
        return response;
    }
}
