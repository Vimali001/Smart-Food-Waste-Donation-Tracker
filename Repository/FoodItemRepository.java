package com.foodtracker.repository;

import com.foodtracker.model.FoodItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.time.LocalDate;
import java.util.List;

@Repository
public interface FoodItemRepository extends JpaRepository<FoodItem, Long> {
    List<FoodItem> findByUserId(Long userId);
    List<FoodItem> findByUserIdAndStatus(Long userId, String status);
    List<FoodItem> findByExpiryDateBetween(LocalDate startDate, LocalDate endDate);
    List<FoodItem> findByUserIdAndExpiryDateBetween(Long userId, LocalDate startDate, LocalDate endDate);
    List<FoodItem> findByCategoryAndStatus(String category, String status);
}
