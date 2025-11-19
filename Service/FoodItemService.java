package com.foodtracker.service;

import com.foodtracker.model.FoodItem;
import com.foodtracker.model.User;
import com.foodtracker.repository.FoodItemRepository;
import com.foodtracker.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class FoodItemService {

    @Autowired
    private FoodItemRepository foodItemRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ExpiryPredictionService expiryPredictionService;

    public FoodItem addFoodItem(Long userId, FoodItem foodItem) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        foodItem.setUser(user);

        // Auto-generate expiry date if not provided
        if (foodItem.getExpiryDate() == null && foodItem.getPurchaseDate() != null) {
            LocalDate predictedExpiry = expiryPredictionService.predictExpiryDate(
                    foodItem.getCategory(), 
                    foodItem.getPurchaseDate()
            );
            foodItem.setExpiryDate(predictedExpiry);
        }

        return foodItemRepository.save(foodItem);
    }

    public FoodItem updateFoodItem(Long id, FoodItem foodItem) {
        FoodItem existingItem = foodItemRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Food item not found"));

        existingItem.setItemName(foodItem.getItemName());
        existingItem.setQuantity(foodItem.getQuantity());
        existingItem.setCategory(foodItem.getCategory());
        existingItem.setPurchaseDate(foodItem.getPurchaseDate());
        existingItem.setExpiryDate(foodItem.getExpiryDate());
        existingItem.setStatus(foodItem.getStatus());

        // Re-predict expiry if category or purchase date changed
        if (foodItem.getExpiryDate() == null && foodItem.getPurchaseDate() != null) {
            LocalDate predictedExpiry = expiryPredictionService.predictExpiryDate(
                    foodItem.getCategory(), 
                    foodItem.getPurchaseDate()
            );
            existingItem.setExpiryDate(predictedExpiry);
        }

        return foodItemRepository.save(existingItem);
    }

    public void deleteFoodItem(Long id) {
        foodItemRepository.deleteById(id);
    }

    public List<FoodItem> getUserFoodItems(Long userId) {
        return foodItemRepository.findByUserId(userId);
    }

    public List<FoodItem> getActiveFoodItems(Long userId) {
        return foodItemRepository.findByUserIdAndStatus(userId, "ACTIVE");
    }

    public FoodItem getFoodItemById(Long id) {
        return foodItemRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Food item not found"));
    }

    public List<FoodItem> getExpiringSoonItems(Long userId, int daysThreshold) {
        LocalDate today = LocalDate.now();
        LocalDate thresholdDate = today.plusDays(daysThreshold);
        List<FoodItem> items = foodItemRepository.findByUserIdAndExpiryDateBetween(
                userId, today, thresholdDate
        );
        
        // Filter by status and check if expiring soon
        return items.stream()
                .filter(item -> "ACTIVE".equals(item.getStatus()))
                .filter(item -> expiryPredictionService.isExpiringSoon(item.getExpiryDate(), daysThreshold))
                .collect(Collectors.toList());
    }
}
