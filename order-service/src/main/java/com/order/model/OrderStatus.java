package com.order.model;

public enum OrderStatus {

    PENDING,      // ordine creato, stock non ancora confermato
    CONFIRMED,    // stock decrementato con successo
    REJECTED,     // stock non disponibile
    CANCELLED     // ordine annullato 
    
}
