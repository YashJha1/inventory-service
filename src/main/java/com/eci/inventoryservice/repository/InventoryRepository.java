package com.eci.inventoryservice.repository;

import com.eci.inventoryservice.model.Inventory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface InventoryRepository extends JpaRepository<Inventory, Long> {
	Optional<Inventory> findByProductIdAndWarehouse(Long productId, String warehouse);

}

