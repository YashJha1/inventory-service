package com.eci.inventoryservice.service;

import com.eci.inventoryservice.model.Inventory;
import com.eci.inventoryservice.repository.InventoryRepository;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class InventoryService {

    private final InventoryRepository repository;

    public InventoryService(InventoryRepository repository) {
        this.repository = repository;
    }

    public List<Inventory> getAllInventory() {
        return repository.findAll();
    }

    public Inventory addInventory(Inventory inventory) {
        return repository.save(inventory);
    }
}

