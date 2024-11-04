package com.api.grocery_booking.controller;

import com.api.grocery_booking.model.Item;
import com.api.grocery_booking.service.GroceryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/admin/grocery-items")
public class AdminController {

    @Autowired
    private GroceryService groceryService;

    @RequestMapping(method = RequestMethod.POST)
    public ResponseEntity<Item> addGroceryItem(@RequestBody Item item) {
        return ResponseEntity.ok(groceryService.addGroceryItem(item));
    }

    @RequestMapping(method = RequestMethod.GET)
    public ResponseEntity<List<Item>> getAllGroceryItems() {
        return ResponseEntity.ok(groceryService.getAllGroceryItems());
    }

    @RequestMapping(path = "/{itemId}", method = RequestMethod.DELETE)
    public ResponseEntity<Void> removeGroceryItem(@PathVariable int itemId) {
        groceryService.removeGroceryItem(itemId);
        return ResponseEntity.noContent().build();
    }

    @RequestMapping(path = "/{itemId}", method = RequestMethod.PUT)
    public ResponseEntity<Item> updateGroceryItem(@PathVariable int itemId, @RequestBody Item item) {
        return ResponseEntity.ok(groceryService.updateGroceryItem(itemId, item));
    }

    @RequestMapping(path = "/{itemId}/inventory", method = RequestMethod.PUT)
    public ResponseEntity<Void> updateInventory(@PathVariable int itemId, @RequestParam int quantity) {
        groceryService.updateInventory(itemId, quantity);
        return ResponseEntity.noContent().build();
    }
}
