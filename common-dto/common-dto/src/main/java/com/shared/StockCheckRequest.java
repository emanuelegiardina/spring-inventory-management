package com.shared;

import java.util.List;

public class StockCheckRequest {

    private List<OrderItemDto> items;

    public StockCheckRequest() {}

    public StockCheckRequest(List<OrderItemDto> items) {
        this.items = items;
    }

    public List<OrderItemDto> getItems() {
        return items;
    }

    public void setItems(List<OrderItemDto> items) {
        this.items = items;
    }
}
