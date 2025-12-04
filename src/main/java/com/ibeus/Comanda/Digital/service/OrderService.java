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

    // Cria um novo pedido a partir dos itens enviados pelo cliente
    // Calcula o total, vincula preços e nomes dos pratos e associa os itens ao pedido
    public Order createOrder(List<OrderItem> items) {
        double total = 0.0;

        for (OrderItem item : items) {
            Dish dish = dishRepository.findById(item.getDishId())
                    .orElseThrow(() -> new RuntimeException("Dish not found: " + item.getDishId()));

            item.setDishName(dish.getName()); // nome do prato exibido no histórico e no carrinho
            item.setUnitPrice(dish.getPrice()); // captura o preço atual do prato
            total += dish.getPrice() * item.getQuantity(); // acumula o valor total do carrinho
        }

        Order order = new Order();
        order.setTotal(total);

        // associa cada item ao pedido criado
        for (OrderItem item : items) {
            item.setOrder(order);
        }

        order.setItems(items);
        return orderRepository.save(order); // persiste o pedido no banco
    }

    // Retorna todos os pedidos cadastrados (usado no admin/cozinha)
    public List<Order> findAll() {
        return orderRepository.findAll();
    }

    // Retorna pedidos filtrados por status (ex.: apenas PREPARING)
    public List<Order> findByStatus(OrderStatus status) {
        return orderRepository.findByStatus(status);
    }

    // Retorna pedidos que estejam em qualquer um dos status informados
    // Útil para cozinha (PREPARING, READY) ou para o motoboy (READY, EN_ROUTE)
    public List<Order> findByStatusIn(List<OrderStatus> statuses) {
        return orderRepository.findByStatusIn(statuses);
    }

    // Atualiza o status do pedido seguindo regras de fluxo (cozinha → entrega → histórico)
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

    // Define quais transições de status são permitidas no fluxo do pedido
    // Ex.: CREATED → PREPARING → READY → EN_ROUTE → DELIVERED
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
