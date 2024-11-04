package com.api.grocery_booking.service.impl;

import com.api.grocery_booking.model.Item;
import com.api.grocery_booking.repository.ItemRepository;
import com.api.grocery_booking.service.GroceryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class GroceryServiceImpl implements GroceryService {

    @Autowired
    private ItemRepository itemRepository;

    @Override
    public Item addGroceryItem(Item item) {
        Item existingItem = itemRepository.findByItemName(item.getItemName());

        if (existingItem != null) {
            existingItem.setQuantity(existingItem.getQuantity() + item.getQuantity());
            existingItem.setPrice(item.getPrice());
            return itemRepository.save(existingItem);
        }

        return itemRepository.save(item);
    }

    @Override
    public List<Item> getAllGroceryItems() {
        return itemRepository.findAll();
    }

    @Override
    public void removeGroceryItem(int itemId) {
        itemRepository.deleteById(itemId);
    }

    @Override
    public Item updateGroceryItem(int itemId, Item item) {
        item.setItemId(itemId);
        return itemRepository.save(item);
    }

    @Override
    public void updateInventory(int id, int quantity) {
        Item item = itemRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Item not found"));
        item.setQuantity(item.getQuantity() + quantity);
        itemRepository.save(item);
    }

    @Override
    public Item findItemByName(String itemName) {
        return itemRepository.findByItemName(itemName);
    }

    public boolean isItemInStock(String itemName, int requestedQuantity) {
        Item item = itemRepository.findByItemName(itemName);
        return item != null && item.getQuantity() >= requestedQuantity;
    }

    public void updateInventoryByItemName(String itemName, int quantitySold) {
        Item item = itemRepository.findByItemName(itemName);
        if (item != null) {
            int newQuantity = item.getQuantity() - quantitySold;
            item.setQuantity(newQuantity);
            itemRepository.save(item);
        }
    }

    public void updateItem(Item item) {
        itemRepository.save(item);
    }

}
