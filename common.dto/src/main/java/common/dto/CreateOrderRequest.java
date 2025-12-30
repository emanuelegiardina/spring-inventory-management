package common.dto;

import java.util.List;

public class CreateOrderRequest {

    private List<OrderItemDto> items;

    public CreateOrderRequest() {
    }

    public CreateOrderRequest(List<OrderItemDto> items) {
        this.items = items;
    }

    public List<OrderItemDto> getItems() {
        return items;
    }

    public void setItems(List<OrderItemDto> items) {
        this.items = items;
    }
}