package com.shared;

public class StockItemResponse {

    private Long productId;
    private boolean available;
    private int availableQuantity;

    public StockItemResponse() {}

    public StockItemResponse(Long productId, boolean available, int availableQuantity) {
        this.productId = productId;
        this.available = available;
        this.availableQuantity = availableQuantity;
    }

    public Long getProductId() {
        return productId;
    }

    public void setProductId(Long productId) {
        this.productId = productId;
    }

    public boolean isAvailable() {
        return available;
    }

    public void setAvailable(boolean available) {
        this.available = available;
    }

    public int getAvailableQuantity() {
        return availableQuantity;
    }

    public void setAvailableQuantity(int availableQuantity) {
        this.availableQuantity = availableQuantity;
    }
}