package common.dto;

import java.util.List;


// classe che contiene elementi del carello
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
