package com.api.grocery_booking.service.impl;

import com.api.grocery_booking.exception.ItemOutOfStockException;
import com.api.grocery_booking.model.Item;
import com.api.grocery_booking.model.Order;
import com.api.grocery_booking.repository.OrderRepository;
import com.api.grocery_booking.service.GroceryService;
import com.api.grocery_booking.service.OrderService;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class OrderServiceImpl implements OrderService {

    @Autowired
    private GroceryService groceryService;

    @Autowired
    private OrderRepository orderRepository;

    @Transactional
    public Order placeOrder(List<Item> items) {
        List<Item> persistedItems = new ArrayList<>();
        List<String> outOfStockItems = new ArrayList<>();

        for (Item item : items) {
            Item existingItem = groceryService.findItemByName(item.getItemName());

            if (existingItem == null || existingItem.getQuantity() < item.getQuantity()) {
                outOfStockItems.add(item.getItemName());
            } else {
                persistedItems.add(existingItem);
            }
        }

        if (!outOfStockItems.isEmpty()) {
            throw new ItemOutOfStockException(outOfStockItems);
        }

        Order order = new Order();
        order.setItems(persistedItems);
        orderRepository.save(order);

        for (Item item : items) {
            Item existingItem = groceryService.findItemByName(item.getItemName());
            if (existingItem != null) {
                existingItem.setQuantity(existingItem.getQuantity() - item.getQuantity());
                groceryService.updateItem(existingItem);
            }
        }

        return order;
    }

}
