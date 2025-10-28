package com.eci.inventoryservice.service;

import com.eci.inventoryservice.model.InventoryMovement;
import com.eci.inventoryservice.repository.InventoryMovementRepository;
import com.eci.inventoryservice.model.Inventory;
import com.eci.inventoryservice.repository.InventoryRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List; // ✅ for List<>
import java.time.LocalDateTime;
import java.util.Optional;

@Service
public class InventoryService {

    /*@Autowired
    private InventoryMovementRepository inventoryMovementRepository;*/ 	
   
    @Autowired
    private InventoryRepository inventoryRepository; // ✅ declare it here

    /*@Transactional
    public String reserveStock(Long productId, String warehouse, Integer quantity) {

        //  Make sure Optional is recognized (imported)
        Optional<Inventory> optInv = inventoryRepository.findByProductIdAndWarehouse(productId, warehouse);

        if (optInv.isEmpty()) {
            return "Inventory record not found for Product ID: " + productId + " at Warehouse: " + warehouse;
        }

        Inventory inventory = optInv.get();
        int available = inventory.getOnHand() - inventory.getReserved();

        if (available < quantity) {
            return "Insufficient stock for Product ID: " + productId;
        }

        //  Use LocalDateTime (imported)
        inventory.setReserved(inventory.getReserved() + quantity);
        inventory.setUpdatedAt(LocalDateTime.now());
        inventoryRepository.save(inventory);

        return "Stock reserved successfully for Product ID: " + productId;
    }*/


    @Autowired
    private InventoryMovementRepository inventoryMovementRepository;
    
    public List<Inventory> getAllInventory() {
    	return inventoryRepository.findAll();
    }
    
    public Optional<Inventory> getInventoryByProduct(Long productId) {
    	return inventoryRepository.findByProductIdAndWarehouse(productId, "WH1");
    }




    @Transactional
    public String reserveStock(Long productId, String warehouse, Integer quantity) {
    	Optional<Inventory> optInv = inventoryRepository.findByProductIdAndWarehouse(productId, warehouse);

    	if (optInv.isEmpty()) {
        	return "Inventory record not found for Product ID: " + productId + " at Warehouse: " + warehouse;
    	}

    	Inventory inventory = optInv.get();
    	int available = inventory.getOnHand() - inventory.getReserved();

    	if (available < quantity) {
        	return "Insufficient stock for Product ID: " + productId;
    	}

    // Update inventory
    	inventory.setReserved(inventory.getReserved() + quantity);
    	inventory.setUpdatedAt(LocalDateTime.now());
    	inventoryRepository.save(inventory);

    // Log movement
    	InventoryMovement movement = new InventoryMovement();
    	movement.setInventoryId(inventory.getInventoryId());
    	movement.setProductId(inventory.getProductId());
    	movement.setWarehouse(inventory.getWarehouse());
    	movement.setChangeType("RESERVE");
    	movement.setQuantity(quantity);
    	inventoryMovementRepository.save(movement);

    	return "Stock reserved successfully for Product ID: " + productId;
    }

    @Transactional
    public String releaseStock(Long productId, String warehouse, Integer quantity) {
    	Optional<Inventory> optInv = inventoryRepository.findByProductIdAndWarehouse(productId, warehouse);

    	if (optInv.isEmpty()) {
        	return "Inventory record not found for Product ID: " + productId + " at Warehouse: " + warehouse;
    	}

    	Inventory inventory = optInv.get();

    	if (inventory.getReserved() < quantity) {
        	return "Cannot release more than reserved quantity for Product ID: " + productId;
    	}

    // Update inventory
    	inventory.setReserved(inventory.getReserved() - quantity);
    	inventory.setUpdatedAt(LocalDateTime.now());
    	inventoryRepository.save(inventory);

    // Log movement
    	InventoryMovement movement = new InventoryMovement();
    	movement.setInventoryId(inventory.getInventoryId());
    	movement.setProductId(inventory.getProductId());
    	movement.setWarehouse(inventory.getWarehouse());
    	movement.setChangeType("RELEASE");
    	movement.setQuantity(quantity);
    	inventoryMovementRepository.save(movement);

    	return "Stock released successfully for Product ID: " + productId;
    }

    
    @Transactional
    public String shipStock(Long productId, String warehouse, Integer quantity) {
    	Optional<Inventory> optInv = inventoryRepository.findByProductIdAndWarehouse(productId, warehouse);

    	if (optInv.isEmpty()) {
        	return "Inventory record not found for Product ID: " + productId + " at Warehouse: " + warehouse;
   	}

    	Inventory inventory = optInv.get();

    // Ensure there is enough reserved to ship
    	if (inventory.getReserved() < quantity) {
        	return "Cannot ship more than reserved quantity for Product ID: " + productId;
    	}

    // Deduct from reserved and on-hand
    	inventory.setReserved(inventory.getReserved() - quantity);
    	inventory.setOnHand(inventory.getOnHand() - quantity);
    	inventory.setUpdatedAt(LocalDateTime.now());
    	inventoryRepository.save(inventory);

    // Log movement
    	InventoryMovement movement = new InventoryMovement();
    	movement.setInventoryId(inventory.getInventoryId());
    	movement.setProductId(inventory.getProductId());
    	movement.setWarehouse(inventory.getWarehouse());
    	movement.setChangeType("SHIP");
    	movement.setQuantity(quantity);
    	inventoryMovementRepository.save(movement);

    	return "Stock shipped successfully for Product ID: " + productId;
    }




}

