package com.foodtracker.service;

import org.springframework.stereotype.Service;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

@Service
public class ExpiryPredictionService {

    /**
     * Predict expiry date based on category and purchase date
     * Rules:
     * - Vegetable = 3 days
     * - Cooked food = 1 day
     * - Packaged = 15 days
     * - Fruit = 5 days
     * - Dairy = 7 days
     * - Default = 7 days
     */
    public LocalDate predictExpiryDate(String category, LocalDate purchaseDate) {
        int daysToAdd = getDaysForCategory(category);
        return purchaseDate.plusDays(daysToAdd);
    }

    /**
     * Get days until expiry based on category
     */
    private int getDaysForCategory(String category) {
        if (category == null || category.trim().isEmpty()) {
            return 7; // Default
        }

        String upperCategory = category.toUpperCase();
        
        switch (upperCategory) {
            case "VEGETABLE":
            case "VEGETABLES":
                return 3;
            case "COOKED_FOOD":
            case "COOKED":
                return 1;
            case "PACKAGED":
            case "PACKAGED_FOOD":
                return 15;
            case "FRUIT":
            case "FRUITS":
                return 5;
            case "DAIRY":
                return 7;
            case "MEAT":
            case "CHICKEN":
                return 2;
            case "BREAD":
                return 3;
            default:
                return 7;
        }
    }

    /**
     * Check if food item is expiring soon (within X days)
     */
    public boolean isExpiringSoon(LocalDate expiryDate, int daysThreshold) {
        if (expiryDate == null) {
            return false;
        }
        LocalDate today = LocalDate.now();
        long daysUntilExpiry = ChronoUnit.DAYS.between(today, expiryDate);
        return daysUntilExpiry >= 0 && daysUntilExpiry <= daysThreshold;
    }

    /**
     * Get days until expiry
     */
    public long getDaysUntilExpiry(LocalDate expiryDate) {
        if (expiryDate == null) {
            return -1;
        }
        LocalDate today = LocalDate.now();
        return ChronoUnit.DAYS.between(today, expiryDate);
    }

    /**
     * Get expiry alert message
     */
    public String getExpiryAlert(LocalDate expiryDate) {
        if (expiryDate == null) {
            return "No expiry date set";
        }

        long daysUntilExpiry = getDaysUntilExpiry(expiryDate);

        if (daysUntilExpiry < 0) {
            return "⚠️ Expired " + Math.abs(daysUntilExpiry) + " days ago";
        } else if (daysUntilExpiry == 0) {
            return "⚠️ Expiring today!";
        } else if (daysUntilExpiry == 1) {
            return "⚠️ Expiring tomorrow!";
        } else if (daysUntilExpiry <= 2) {
            return "⚠️ Expiring in " + daysUntilExpiry + " days";
        } else if (daysUntilExpiry <= 7) {
            return "⚠️ Expiring in " + daysUntilExpiry + " days";
        } else {
            return "✅ Expires in " + daysUntilExpiry + " days";
        }
    }
}
