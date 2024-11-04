package com.api.grocery_booking.service;

import com.api.grocery_booking.model.Item;
import com.api.grocery_booking.model.Order;

import java.util.List;

public interface OrderService {
    Order placeOrder(List<Item> itemList);
}
