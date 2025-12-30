package common.dto;

public class CreateOrderResponse {

    private Long orderId;
    private String message;

    public CreateOrderResponse() {
    }

    public CreateOrderResponse(Long orderId, String message) {
        this.orderId = orderId;
        this.message = message;
    }

    public Long getOrderId() {
        return orderId;
    }

    public void setOrderId(Long orderId) {
        this.orderId = orderId;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}