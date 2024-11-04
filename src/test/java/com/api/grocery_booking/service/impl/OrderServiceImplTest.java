package com.api.grocery_booking.service.impl;

import com.api.grocery_booking.exception.ItemOutOfStockException;
import com.api.grocery_booking.model.Item;
import com.api.grocery_booking.model.Order;
import com.api.grocery_booking.repository.OrderRepository;
import com.api.grocery_booking.service.GroceryService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class OrderServiceImplTest {

    @Mock
    private GroceryService groceryService;

    @Mock
    private OrderRepository orderRepository;

    @InjectMocks
    private OrderServiceImpl orderService;

    @BeforeEach
    public void setUp() {
    }

    @Test
    public void testPlaceOrder_Success() {
        Item item1 = new Item("Apple", 0.5, 10);
        Item item2 = new Item("Banana", 0.3, 5);
        List<Item> itemsToOrder = Arrays.asList(
                new Item("Apple", 0.5, 5),
                new Item("Banana", 0.3, 3)
        );

        when(groceryService.findItemByName("Apple")).thenReturn(item1);
        when(groceryService.findItemByName("Banana")).thenReturn(item2);
        when(orderRepository.save(any(Order.class))).thenAnswer(invocation -> {
            Order order = invocation.getArgument(0);
            order.setOrderId(1);
            return order;
        });

        Order result = orderService.placeOrder(itemsToOrder);

        assertNotNull(result);
        assertEquals(2, result.getItems().size());
        assertEquals(5, item1.getQuantity());
        assertEquals(2, item2.getQuantity());
        verify(orderRepository).save(any(Order.class));
    }

    @Test
    public void testPlaceOrder_SomeItemsOutOfStock() {
        Item item1 = new Item("Apple", 0.5, 10);
        List<Item> itemsToOrder = Arrays.asList(
                new Item("Apple", 0.5, 5),
                new Item("Banana", 0.3, 15)
        );

        when(groceryService.findItemByName("Apple")).thenReturn(item1);
        when(groceryService.findItemByName("Banana")).thenReturn(null);

        ItemOutOfStockException exception = assertThrows(ItemOutOfStockException.class, () -> {
            orderService.placeOrder(itemsToOrder);
        });
        assertTrue(exception.getOutOfStockItems().contains("Banana"));
        verify(orderRepository, never()).save(any(Order.class));
    }

    @Test
    public void testPlaceOrder_NoItemsFound() {
        List<Item> itemsToOrder = Arrays.asList(
                new Item("Apple", 0.5, 5),
                new Item("Banana", 0.3, 3)
        );

        when(groceryService.findItemByName("Apple")).thenReturn(null);
        when(groceryService.findItemByName("Banana")).thenReturn(null);

        ItemOutOfStockException exception = assertThrows(ItemOutOfStockException.class, () -> {
            orderService.placeOrder(itemsToOrder);
        });
        assertTrue(exception.getOutOfStockItems().contains("Apple"));
        assertTrue(exception.getOutOfStockItems().contains("Banana"));
        verify(orderRepository, never()).save(any(Order.class));
    }

    @Test
    public void testPlaceOrder_ItemNotFoundInGroceryService() {
        List<Item> itemsToOrder = Arrays.asList(
                new Item("Apple", 0.5, 5),
                new Item("Banana", 0.3, 3)
        );

        when(groceryService.findItemByName("Apple")).thenReturn(null);
        when(groceryService.findItemByName("Banana")).thenReturn(null);

        ItemOutOfStockException exception = assertThrows(ItemOutOfStockException.class, () -> {
            orderService.placeOrder(itemsToOrder);
        });

        assertTrue(exception.getOutOfStockItems().contains("Apple"));
        assertTrue(exception.getOutOfStockItems().contains("Banana"));
        verify(groceryService, never()).updateItem(any(Item.class));
    }
}
