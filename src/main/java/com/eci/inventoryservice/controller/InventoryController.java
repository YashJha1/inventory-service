package com.eci.inventoryservice.controller;
import java.util.List; // ✅ for List<>
import com.eci.inventoryservice.model.Inventory; // ✅ for Inventory model
import java.util.Optional; // ✅ Add this import
import com.eci.inventoryservice.dto.ReserveRequest;
import com.eci.inventoryservice.service.InventoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/v1/inventory")
public class InventoryController {

    @Autowired
    private InventoryService inventoryService; // 👈 required variable

    @PostMapping("/reserve")
    public ResponseEntity<String> reserveStock(@RequestBody ReserveRequest request) {
        String result = inventoryService.reserveStock(
                request.getProductId(),
                request.getWarehouse(),
                request.getQuantity()
        );

        if (result.startsWith("Insufficient") || result.startsWith("Inventory record not found")) {
            return ResponseEntity.badRequest().body(result);
        }
        return ResponseEntity.ok(result);
    }

    @PostMapping("/release")
    public ResponseEntity<String> releaseStock(@RequestBody ReserveRequest request) {
    	String result = inventoryService.releaseStock(
        	    request.getProductId(),
            	    request.getWarehouse(),
                    request.getQuantity()
        );

    	if (result.startsWith("Cannot") || result.startsWith("Inventory record not found")) {
        	return ResponseEntity.badRequest().body(result);
    	}
    	return ResponseEntity.ok(result);
    }

    @PostMapping("/ship")
    public ResponseEntity<String> shipStock(@RequestBody ReserveRequest request) {
    	String result = inventoryService.shipStock(
        	    request.getProductId(),
            	    request.getWarehouse(),
                    request.getQuantity()
    	);

    	if (result.startsWith("Cannot") || result.startsWith("Inventory record not found")) {
        	return ResponseEntity.badRequest().body(result);
    	}
    	return ResponseEntity.ok(result);
    }

    @GetMapping
    public ResponseEntity<List<Inventory>> getAllInventory() {
    	List<Inventory> inventories = inventoryService.getAllInventory();
    	return ResponseEntity.ok(inventories);
    }

    @GetMapping("/{productId}")
    public ResponseEntity<Inventory> getInventoryByProduct(@PathVariable Long productId) {
    	Optional<Inventory> inv = inventoryService.getInventoryByProduct(productId);
    	return inv.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }



}

