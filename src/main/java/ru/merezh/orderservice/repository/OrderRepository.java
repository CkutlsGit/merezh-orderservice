package ru.merezh.orderservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.merezh.orderservice.entity.Order;

import java.util.List;
import java.util.Optional;

public interface OrderRepository extends JpaRepository<Order, Long> {
    Optional<Order> getOrderByOrderId(long id);
    List<Order> getOrdersByUserId(long userId);
}
