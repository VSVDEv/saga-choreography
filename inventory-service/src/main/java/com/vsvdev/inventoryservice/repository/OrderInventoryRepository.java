package com.vsvdev.inventoryservice.repository;

import com.vsvdev.inventoryservice.entity.OrderInventory;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderInventoryRepository extends JpaRepository<OrderInventory, Integer> {
}
