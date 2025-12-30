package common.dto;

import java.util.List;

public class ConfirmOrderClientResponse {

    private Long orderId;
    private String status;
    private String message;
    private List<OrderItemDto> unavailableItems;

    
    public ConfirmOrderClientResponse(Long orderId, String status, String message,
            List<OrderItemDto> unavailableItems) {
        this.orderId = orderId;
        this.status = status;
        this.message = message;
        this.unavailableItems = unavailableItems;
    }
    public Long getOrderId() {
        return orderId;
    }
    public void setOrderId(Long orderId) {
        this.orderId = orderId;
    }
    public String getStatus() {
        return status;
    }
    public void setStatus(String status) {
        this.status = status;
    }
    public String getMessage() {
        return message;
    }
    public void setMessage(String message) {
        this.message = message;
    }
    public List<OrderItemDto> getUnavailableItems() {
        return unavailableItems;
    }
    public void setUnavailableItems(List<OrderItemDto> unavailableItems) {
        this.unavailableItems = unavailableItems;
    }

    
}
