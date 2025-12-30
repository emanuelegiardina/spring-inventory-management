package common.dto;

import java.util.List;

public class ConfirmOrderResponse {

    private boolean success;
    private String message;
    private List<OrderItemDto> unavailableItems;
    public boolean isSuccess() {
        return success;
    }
    public void setSuccess(boolean success) {
        this.success = success;
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

    // getters & setters
    
}
