package com.api.grocery_booking.exception;

import java.util.List;

public class ItemOutOfStockException extends RuntimeException {
    private List<String> outOfStockItems;

    public ItemOutOfStockException(List<String> outOfStockItems) {
        super("The following items are out of stock: " + String.join(", ", outOfStockItems));
        this.outOfStockItems = outOfStockItems;
    }

    public ItemOutOfStockException(String message){
        super(message);
    }

    public List<String> getOutOfStockItems() {
        return outOfStockItems;
    }
}
