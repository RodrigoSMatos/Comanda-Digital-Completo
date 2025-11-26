package com.ibeus.Comanda.Digital.controller;

import com.ibeus.Comanda.Digital.model.Order;
import com.ibeus.Comanda.Digital.model.OrderItem;
import com.ibeus.Comanda.Digital.model.OrderStatus;
import com.ibeus.Comanda.Digital.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/orders")
@CrossOrigin(origins = "http://localhost:4200")
public class OrderController {

    @Autowired
    private OrderService orderService;

    // Criar pedido: body é uma lista de itens com dishId e quantity
    @PostMapping
    public ResponseEntity<Order> create(@RequestBody List<OrderItem> items) {
        if (items == null || items.isEmpty()) {
            return ResponseEntity.badRequest().build();
        }
        boolean hasInvalidQty = items.stream()
                .anyMatch(i -> i.getDishId() == null || i.getQuantity() == null || i.getQuantity() <= 0);
        if (hasInvalidQty) {
            return ResponseEntity.badRequest().build();
        }
        Order created = orderService.createOrder(items);
        return ResponseEntity.ok(created);
    }

    // Listar pedidos, com filtro opcional de status
    // Exemplos:
    //   GET /orders
    //   GET /orders?status=CREATED
    //   GET /orders?status=CREATED,PREPARING
    @GetMapping
    public ResponseEntity<List<Order>> getAll(@RequestParam(required = false) String status) {
        if (status == null || status.isBlank()) {
            return ResponseEntity.ok(orderService.findAll());
        }

        // Suporta múltiplos status separados por vírgula
        List<OrderStatus> statuses = Arrays.stream(status.split(","))
                .map(String::trim)
                .filter(s -> !s.isEmpty())
                .map(s -> {
                    try { return OrderStatus.valueOf(s); }
                    catch (IllegalArgumentException ex) { return null; }
                })
                .filter(Objects::nonNull)
                .collect(Collectors.toList());

        if (statuses.isEmpty()) {
            return ResponseEntity.badRequest().build();
        }

        if (statuses.size() == 1) {
            return ResponseEntity.ok(orderService.findByStatus(statuses.get(0)));
        } else {
            return ResponseEntity.ok(orderService.findByStatusIn(statuses));
        }
    }

    // Buscar por ID
    @GetMapping("/{id}")
    public ResponseEntity<Order> getById(@PathVariable Long id) {
        return ResponseEntity.ok(orderService.findById(id));
    }

    // Atualizar status: /orders/{id}/status?value=PREPARING
    @PatchMapping("/{id}/status")
    public ResponseEntity<Order> updateStatus(@PathVariable Long id, @RequestParam String value) {
        OrderStatus newStatus;
        try {
            newStatus = OrderStatus.valueOf(value);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        }

        // (Simples) Não valida transição aqui para manter MVP enxuto.
        // Caso queira travar o fluxo, podemos mover a validação para o service.
        Order updated = orderService.updateStatus(id, newStatus);
        return ResponseEntity.ok(updated);
    }
}
