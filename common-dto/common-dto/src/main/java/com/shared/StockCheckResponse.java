package com.shared;

import java.util.List;

public class StockCheckResponse {

    private boolean allAvailable;
    private List<StockItemResponse> items;
    private String message;

    public StockCheckResponse() {}

    public StockCheckResponse(boolean allAvailable, List<StockItemResponse> items) {
        this.allAvailable = allAvailable;
        this.items = items;
    }

    public void setMessage(String message){
         this.message= message;
    }

    public String getMessage(){
        return message;
    }

    public boolean isAllAvailable() {
        return allAvailable;
    }

    public void setAllAvailable(boolean allAvailable) {
        this.allAvailable = allAvailable;
    }

    public List<StockItemResponse> getItems() {
        return items;
    }

    public void setItems(List<StockItemResponse> items) {
        this.items = items;
    }
}