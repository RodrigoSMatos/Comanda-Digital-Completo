package com.ibeus.Comanda.Digital.service;

import com.ibeus.Comanda.Digital.model.*;
import com.ibeus.Comanda.Digital.repository.DishRepository;
import com.ibeus.Comanda.Digital.repository.OrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class OrderService {

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private DishRepository dishRepository;

    // Criar pedido
    public Order createOrder(List<OrderItem> items) {
        double total = 0.0;

        for (OrderItem item : items) {
            Dish dish = dishRepository.findById(item.getDishId())
                    .orElseThrow(() -> new RuntimeException("Dish not found: " + item.getDishId()));

            item.setDishName(dish.getName());
            item.setUnitPrice(dish.getPrice());
            total += dish.getPrice() * item.getQuantity();
        }

        Order order = new Order();
        order.setTotal(total);

        // associa cada item ao pedido
        for (OrderItem item : items) {
            item.setOrder(order);
        }

        order.setItems(items);
        return orderRepository.save(order);
    }

    // Buscar todos os pedidos
    public List<Order> findAll() {
        return orderRepository.findAll();
    }

    // Buscar por status
    public List<Order> findByStatus(OrderStatus status) {
        return orderRepository.findByStatus(status);
    }

    // Buscar por múltiplos status (usado na cozinha/motoboy)
    public List<Order> findByStatusIn(List<OrderStatus> statuses) {
        return orderRepository.findByStatusIn(statuses);
    }

    // OrderService.java
    public Order updateStatus(Long id, OrderStatus newStatus) {
        Order order = orderRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Order not found"));

        OrderStatus current = order.getStatus();
        if (!isValidTransition(current, newStatus)) {
            // Você pode lançar uma exception customizada e tratar no Controller,
            // aqui simplifico para 409 Conflict via ResponseStatusException:
            throw new org.springframework.web.server.ResponseStatusException(
                    org.springframework.http.HttpStatus.CONFLICT,
                    "Transição de status inválida: " + current + " -> " + newStatus
            );
        }

        order.setStatus(newStatus);
        return orderRepository.save(order);
    }

    // Permite somente as transições do fluxo definido
    private boolean isValidTransition(OrderStatus current, OrderStatus next) {
        switch (current) {
            case CREATED:
                return next == OrderStatus.PREPARING; // (Cozinha)
            case PREPARING:
                return next == OrderStatus.READY_FOR_DELIVERY; // (Cozinha)
            case READY_FOR_DELIVERY:
                return next == OrderStatus.EN_ROUTE; // (Motoboy)
            case EN_ROUTE:
                return next == OrderStatus.DELIVERED; // (Motoboy)
            case DELIVERED:
                return false; // estado final
            default:
                return false;
        }
    }

    // Buscar por ID
    public Order findById(Long id) {
        return orderRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Order not found"));
    }
}
