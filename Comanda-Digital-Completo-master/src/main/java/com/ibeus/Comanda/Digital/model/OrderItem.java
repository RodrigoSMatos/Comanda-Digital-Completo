package com.ibeus.Comanda.Digital.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "order_items")
@Data
public class OrderItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long dishId;        // ID do prato no cardápio
    private String dishName;    // Nome do prato no momento do pedido
    private Double unitPrice;   // Preço do prato no momento do pedido
    private Integer quantity;   // Quantidade pedida

    @ManyToOne
    @JsonIgnore
    @JoinColumn(name = "order_id")
    private Order order;        // Ligação com o pedido
}
