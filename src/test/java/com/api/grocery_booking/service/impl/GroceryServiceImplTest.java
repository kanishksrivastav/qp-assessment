package com.api.grocery_booking.service.impl;

import com.api.grocery_booking.model.Item;
import com.api.grocery_booking.repository.ItemRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class GroceryServiceImplTest {

    @InjectMocks
    private GroceryServiceImpl groceryService;

    @Mock
    private ItemRepository itemRepository;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testAddGroceryItem_NewItem() {
        Item item = new Item("Apple", 0.5, 10);
        when(itemRepository.findByItemName(item.getItemName())).thenReturn(null);
        when(itemRepository.save(item)).thenReturn(item);

        Item result = groceryService.addGroceryItem(item);

        assertEquals(item, result);
        verify(itemRepository).save(item);
    }

    @Test
    public void testAddGroceryItem_ExistingItem() {
        Item existingItem = new Item("Apple", 0.5, 5);
        Item newItem = new Item("Apple", 0.5, 10);
        when(itemRepository.findByItemName(newItem.getItemName())).thenReturn(existingItem);
        when(itemRepository.save(existingItem)).thenReturn(existingItem);

        Item result = groceryService.addGroceryItem(newItem);

        assertEquals(existingItem.getQuantity(), result.getQuantity());
        assertEquals(15, existingItem.getQuantity()); // Quantity should be updated
        verify(itemRepository).save(existingItem);
    }

    @Test
    public void testGetAllGroceryItems() {
        List<Item> items = new ArrayList<>();
        items.add(new Item("Apple", 0.5, 10));
        when(itemRepository.findAll()).thenReturn(items);

        List<Item> result = groceryService.getAllGroceryItems();

        assertEquals(items.size(), result.size());
        verify(itemRepository).findAll();
    }

    @Test
    public void testRemoveGroceryItem() {
        int itemId = 1;
        doNothing().when(itemRepository).deleteById(itemId);

        groceryService.removeGroceryItem(itemId);

        verify(itemRepository).deleteById(itemId);
    }

    @Test
    public void testUpdateGroceryItem() {
        Item item = new Item("Apple", 0.5, 10);
        item.setItemId(1);
        when(itemRepository.save(item)).thenReturn(item);

        Item result = groceryService.updateGroceryItem(item.getItemId(), item);

        assertEquals(item, result);
        verify(itemRepository).save(item);
    }

    @Test
    public void testUpdateInventory_ItemExists() {
        Item item = new Item("Apple", 0.5, 10);
        item.setItemId(1);
        when(itemRepository.findById(item.getItemId())).thenReturn(Optional.of(item));
        when(itemRepository.save(item)).thenReturn(item);

        groceryService.updateInventory(item.getItemId(), 5);

        assertEquals(15, item.getQuantity());
        verify(itemRepository).save(item);
    }

    @Test
    public void testUpdateInventory_ItemNotFound() {
        int itemId = 1;
        when(itemRepository.findById(itemId)).thenReturn(Optional.empty());

        Exception exception = assertThrows(RuntimeException.class, () -> {
            groceryService.updateInventory(itemId, 5);
        });

        assertEquals("Item not found", exception.getMessage());
    }

    @Test
    public void testFindItemByName() {
        Item item = new Item("Apple", 0.5, 10);
        when(itemRepository.findByItemName("Apple")).thenReturn(item);

        Item result = groceryService.findItemByName("Apple");

        assertEquals(item, result);
        verify(itemRepository).findByItemName("Apple");
    }

    @Test
    public void testIsItemInStock_ItemInStock() {
        Item item = new Item("Apple", 0.5, 10);
        when(itemRepository.findByItemName("Apple")).thenReturn(item);

        boolean result = groceryService.isItemInStock("Apple", 5);

        assertTrue(result);
    }

    @Test
    public void testIsItemInStock_ItemOutOfStock() {
        Item item = new Item("Apple", 0.5, 3);
        when(itemRepository.findByItemName("Apple")).thenReturn(item);

        boolean result = groceryService.isItemInStock("Apple", 5);

        assertFalse(result);
    }

    @Test
    public void testIsItemInStock_ItemDoesNotExist() {
        String itemName = "Banana";
        int requestedQuantity = 5;
        when(itemRepository.findByItemName(itemName)).thenReturn(null);

        boolean result = groceryService.isItemInStock(itemName, requestedQuantity);

        assertFalse(result);
        verify(itemRepository).findByItemName(itemName);
    }
    @Test
    public void testUpdateInventoryByItemName_ItemExists() {
        Item item = new Item("Apple", 0.5, 10);
        when(itemRepository.findByItemName("Apple")).thenReturn(item);
        when(itemRepository.save(item)).thenReturn(item);

        groceryService.updateInventoryByItemName("Apple", 5);

        assertEquals(5, item.getQuantity());
        verify(itemRepository).save(item);
    }

    @Test
    public void testUpdateInventoryByItemName_ItemNotFound() {
        when(itemRepository.findByItemName("Apple")).thenReturn(null);

        groceryService.updateInventoryByItemName("Apple", 5);

        verify(itemRepository, never()).save(any());
    }

    @Test
    public void testUpdateItem() {
        Item item = new Item("Apple", 0.5, 10);
        when(itemRepository.save(item)).thenReturn(item);

        groceryService.updateItem(item);

        verify(itemRepository).save(item);
    }
}

