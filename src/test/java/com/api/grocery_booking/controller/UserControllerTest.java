package com.api.grocery_booking.controller;

import com.api.grocery_booking.exception.ItemOutOfStockException;
import com.api.grocery_booking.model.Item;
import com.api.grocery_booking.model.Order;
import com.api.grocery_booking.service.GroceryService;
import com.api.grocery_booking.service.OrderService;
import org.junit.jupiter.api.Test;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(UserController.class)
@MockitoSettings(strictness = Strictness.LENIENT)
public class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private GroceryService groceryService;

    @MockBean
    private OrderService orderService;

    @Test
    public void testGetAvailableGroceryItems() throws Exception {
        List<Item> items = Arrays.asList(new Item("Apple", 0.5, 100), new Item("Banana", 0.3, 150));
        when(groceryService.getAllGroceryItems()).thenReturn(items);

        mockMvc.perform(get("/user/grocery-items")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json("[{\"itemName\":\"Apple\",\"price\":0.5,\"quantity\":100}," +
                        "{\"itemName\":\"Banana\",\"price\":0.3,\"quantity\":150}]"));
    }

    @Test
    public void testBookGroceryItems_Success() throws Exception {
        List<Item> items = Arrays.asList(new Item("Apple", 0.5, 10));
        Order order = new Order();
        order.setOrderId(1);
        order.setItems(items);

        when(orderService.placeOrder(any())).thenReturn(order);

        mockMvc.perform(post("/user/grocery-items/orders")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("[{\"itemName\":\"Apple\",\"quantity\":10}]"))
                .andExpect(status().isOk())
                .andExpect(content().json("{\"orderId\":1,\"items\":[{\"itemName\":\"Apple\",\"price\":0.5,\"quantity\":10}]}"));
    }

    @Test
    public void testBookGroceryItems_OutOfStock() throws Exception {
        List<Item> items = Arrays.asList(new Item("Apple", 0.5, 10));
        when(orderService.placeOrder(any())).thenThrow(new ItemOutOfStockException("Apple is out of stock"));

        mockMvc.perform(post("/user/grocery-items/orders")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("[{\"itemName\":\"Apple\",\"quantity\":10}]"))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("Apple is out of stock"));
    }
}
