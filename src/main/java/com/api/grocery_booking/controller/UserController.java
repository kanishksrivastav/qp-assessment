package com.api.grocery_booking.controller;

import com.api.grocery_booking.model.Item;
import com.api.grocery_booking.model.Order;
import com.api.grocery_booking.service.GroceryService;
import com.api.grocery_booking.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/user/grocery-items")
public class UserController {
    @Autowired
    private GroceryService groceryService;

    @Autowired
    private OrderService orderService;

    @RequestMapping(method = RequestMethod.GET)
    public ResponseEntity<List<Item>> getAvailableGroceryItems() {
        return ResponseEntity.ok(groceryService.getAllGroceryItems());
    }

    @RequestMapping(path = "/orders", method = RequestMethod.POST)
    public ResponseEntity<Order> bookGroceryItems(@RequestBody List<Item> items) {
        return ResponseEntity.ok(orderService.placeOrder(items));
    }
}
