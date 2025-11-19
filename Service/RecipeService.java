package com.foodtracker.service;

import com.foodtracker.model.FoodItem;
import com.foodtracker.model.Recipe;
import com.foodtracker.repository.FoodItemRepository;
import com.foodtracker.repository.RecipeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class RecipeService {

    @Autowired
    private RecipeRepository recipeRepository;

    @Autowired
    private FoodItemRepository foodItemRepository;

    public List<Recipe> getAllRecipes() {
        return recipeRepository.findAll();
    }

    public List<Recipe> getRecipesByCategory(String category) {
        return recipeRepository.findByCategory(category);
    }

    public Recipe addRecipe(Recipe recipe) {
        return recipeRepository.save(recipe);
    }

    /**
     * Suggest recipes based on expiring food items
     */
    public List<Recipe> suggestRecipesForExpiringItems(Long userId) {
        List<FoodItem> expiringItems = foodItemRepository.findByUserId(userId).stream()
                .filter(item -> "ACTIVE".equals(item.getStatus()))
                .collect(Collectors.toList());

        List<Recipe> suggestedRecipes = new ArrayList<>();

        for (FoodItem item : expiringItems) {
            // Find recipes by category
            if (item.getCategory() != null && !item.getCategory().isEmpty()) {
                List<Recipe> categoryRecipes = recipeRepository.findByCategory(item.getCategory());
                suggestedRecipes.addAll(categoryRecipes);
            }

            // Find recipes by ingredient name
            List<Recipe> ingredientRecipes = recipeRepository.findByIngredientsContaining(
                    item.getItemName().toLowerCase()
            );
            suggestedRecipes.addAll(ingredientRecipes);
        }

        // Remove duplicates
        return suggestedRecipes.stream()
                .distinct()
                .collect(Collectors.toList());
    }

    public Recipe getRecipeById(Long id) {
        return recipeRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Recipe not found"));
    }
}
