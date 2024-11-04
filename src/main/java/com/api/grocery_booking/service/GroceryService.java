package com.api.grocery_booking.service;

import com.api.grocery_booking.model.Item;

import java.util.List;

public interface GroceryService {

    Item addGroceryItem(Item item);
    List<Item> getAllGroceryItems();
    void removeGroceryItem(int itemId);
    Item updateGroceryItem(int itemId, Item item);
    void updateInventory(int id, int quantity);
    Item findItemByName(String itemName);
    boolean isItemInStock(String itemName, int requestedQuantity);
    void updateInventoryByItemName(String itemName, int quantitySold);
    void updateItem(Item item);
}
