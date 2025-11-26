package com.ibeus.Comanda.Digital.model;

public enum OrderStatus {
    CREATED,             // Pedido criado (cliente finalizou)
    PREPARING,           // Cozinha preparando
    READY_FOR_DELIVERY,  // Pronto para entrega (aguardando motoboy)
    EN_ROUTE,            // Motoboy a caminho
    DELIVERED            // Entregue
}
