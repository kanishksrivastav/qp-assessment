package com.api.grocery_booking.repository;

import com.api.grocery_booking.model.Item;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ItemRepository extends JpaRepository<Item,Integer> {
    Item findByItemName(String itemName);
}
