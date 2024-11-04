package com.api.grocery_booking.controller;

import com.api.grocery_booking.model.Item;
import com.api.grocery_booking.service.GroceryService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(AdminController.class)
public class AdminControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private GroceryService groceryService;

    private Item item;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        item = new Item();
        item.setItemId(1);
        item.setItemName("Apple");
        item.setPrice(0.5);
        item.setQuantity(100);
    }

    @Test
    public void testAddGroceryItem() throws Exception {
        when(groceryService.addGroceryItem(any(Item.class))).thenReturn(item);

        mockMvc.perform(post("/admin/grocery-items")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"itemName\":\"Apple\",\"price\":0.5,\"quantity\":100}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.itemName").value("Apple"));
    }

    @Test
    public void testGetAllGroceryItems() throws Exception {
        when(groceryService.getAllGroceryItems()).thenReturn(List.of(item));

        mockMvc.perform(get("/admin/grocery-items")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].itemName").value("Apple"));
    }

    @Test
    public void testRemoveGroceryItem() throws Exception {
        doNothing().when(groceryService).removeGroceryItem(1);

        mockMvc.perform(delete("/admin/grocery-items/{itemId}", 1))
                .andExpect(status().isNoContent());
    }

    @Test
    public void testUpdateGroceryItem() throws Exception {
        when(groceryService.updateGroceryItem(eq(1), any(Item.class))).thenAnswer(invocation -> {
            Item itemToUpdate = invocation.getArgument(1);
            item.setQuantity(itemToUpdate.getQuantity());
            return item;
        });

        mockMvc.perform(put("/admin/grocery-items/{itemId}", 1)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"itemName\":\"Apple\",\"price\":0.5,\"quantity\":150}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.quantity").value(150));
    }

    @Test
    public void testUpdateInventory() throws Exception {
        int itemId = 1;
        int quantity = 50;

        doNothing().when(groceryService).updateInventory(itemId, quantity);

        mockMvc.perform(put("/admin/grocery-items/{itemId}/inventory", itemId)
                        .param("quantity", String.valueOf(quantity)))
                .andExpect(status().isNoContent());

        verify(groceryService).updateInventory(itemId, quantity);
    }
}
