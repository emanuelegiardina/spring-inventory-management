package common.dto;

public class OrderItemDto {

    private Long productId;
    private int quantity;

    public OrderItemDto() {}

    public OrderItemDto(Long productId, int quantity) {
        this.productId = productId;
        this.quantity = quantity;
    }

    public Long getProductId() {
        return productId;
    }

    public void setProductId(Long productId) {
        this.productId = productId;
    }

    public int getQuantity() {
        return quantity;
    }
}
