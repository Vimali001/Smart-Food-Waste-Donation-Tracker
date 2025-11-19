package com.foodtracker.controller;

import com.foodtracker.model.FoodItem;
import com.foodtracker.service.FoodItemService;
import com.foodtracker.service.ExpiryPredictionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import jakarta.servlet.http.HttpSession;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/food-items")
@CrossOrigin(origins = "*")
public class FoodItemController {

    @Autowired
    private FoodItemService foodItemService;

    @Autowired
    private ExpiryPredictionService expiryPredictionService;

    @PostMapping
    public ResponseEntity<?> addFoodItem(@RequestBody FoodItem foodItem, HttpSession session) {
        Long userId = (Long) session.getAttribute("userId");
        if (userId == null) {
            return ResponseEntity.badRequest().body(Map.of(
                "success", false,
                "message", "Please login first"
            ));
        }
        try {
            FoodItem savedItem = foodItemService.addFoodItem(userId, foodItem);
            String alert = expiryPredictionService.getExpiryAlert(savedItem.getExpiryDate());
            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("message", "Food item added successfully");
            response.put("foodItem", savedItem);
            response.put("expiryAlert", alert);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of(
                "success", false,
                "message", e.getMessage()
            ));
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateFoodItem(@PathVariable Long id, @RequestBody FoodItem foodItem, HttpSession session) {
        Long userId = (Long) session.getAttribute("userId");
        if (userId == null) {
            return ResponseEntity.badRequest().body(Map.of(
                "success", false,
                "message", "Please login first"
            ));
        }
        try {
            FoodItem updatedItem = foodItemService.updateFoodItem(id, foodItem);
            String alert = expiryPredictionService.getExpiryAlert(updatedItem.getExpiryDate());
            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("message", "Food item updated successfully");
            response.put("foodItem", updatedItem);
            response.put("expiryAlert", alert);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of(
                "success", false,
                "message", e.getMessage()
            ));
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteFoodItem(@PathVariable Long id, HttpSession session) {
        Long userId = (Long) session.getAttribute("userId");
        if (userId == null) {
            return ResponseEntity.badRequest().body(Map.of(
                "success", false,
                "message", "Please login first"
            ));
        }
        try {
            foodItemService.deleteFoodItem(id);
            return ResponseEntity.ok(Map.of(
                "success", true,
                "message", "Food item deleted successfully"
            ));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of(
                "success", false,
                "message", e.getMessage()
            ));
        }
    }

    @GetMapping
    public ResponseEntity<?> getUserFoodItems(HttpSession session) {
        Long userId = (Long) session.getAttribute("userId");
        if (userId == null) {
            return ResponseEntity.badRequest().body(Map.of(
                "success", false,
                "message", "Please login first"
            ));
        }
        try {
            List<FoodItem> items = foodItemService.getUserFoodItems(userId);
            return ResponseEntity.ok(items);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of(
                "success", false,
                "message", e.getMessage()
            ));
        }
    }

    @GetMapping("/active")
    public ResponseEntity<?> getActiveFoodItems(HttpSession session) {
        Long userId = (Long) session.getAttribute("userId");
        if (userId == null) {
            return ResponseEntity.badRequest().body(Map.of(
                "success", false,
                "message", "Please login first"
            ));
        }
        try {
            List<FoodItem> items = foodItemService.getActiveFoodItems(userId);
            return ResponseEntity.ok(items);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of(
                "success", false,
                "message", e.getMessage()
            ));
        }
    }

    @GetMapping("/expiring-soon")
    public ResponseEntity<?> getExpiringSoonItems(
            @RequestParam(defaultValue = "2") int days,
            HttpSession session) {
        Long userId = (Long) session.getAttribute("userId");
        if (userId == null) {
            return ResponseEntity.badRequest().body(Map.of(
                "success", false,
                "message", "Please login first"
            ));
        }
        try {
            List<FoodItem> items = foodItemService.getExpiringSoonItems(userId, days);
            return ResponseEntity.ok(items);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of(
                "success", false,
                "message", e.getMessage()
            ));
        }
    }

    @GetMapping("/{id}/alert")
    public ResponseEntity<?> getExpiryAlert(@PathVariable Long id, HttpSession session) {
        Long userId = (Long) session.getAttribute("userId");
        if (userId == null) {
            return ResponseEntity.badRequest().body(Map.of(
                "success", false,
                "message", "Please login first"
            ));
        }
        try {
            FoodItem item = foodItemService.getFoodItemById(id);
            String alert = expiryPredictionService.getExpiryAlert(item.getExpiryDate());
            return ResponseEntity.ok(Map.of(
                "foodItem", item,
                "expiryAlert", alert,
                "daysUntilExpiry", expiryPredictionService.getDaysUntilExpiry(item.getExpiryDate())
            ));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of(
                "success", false,
                "message", e.getMessage()
            ));
        }
    }
}
