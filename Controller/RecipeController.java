package com.foodtracker.controller;

import com.foodtracker.model.Recipe;
import com.foodtracker.service.RecipeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import jakarta.servlet.http.HttpSession;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/recipes")
@CrossOrigin(origins = "*")
public class RecipeController {

    @Autowired
    private RecipeService recipeService;

    @GetMapping
    public ResponseEntity<?> getAllRecipes() {
        try {
            List<Recipe> recipes = recipeService.getAllRecipes();
            return ResponseEntity.ok(recipes);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of(
                "success", false,
                "message", e.getMessage()
            ));
        }
    }

    @GetMapping("/category/{category}")
    public ResponseEntity<?> getRecipesByCategory(@PathVariable String category) {
        try {
            List<Recipe> recipes = recipeService.getRecipesByCategory(category);
            return ResponseEntity.ok(recipes);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of(
                "success", false,
                "message", e.getMessage()
            ));
        }
    }

    @GetMapping("/suggest")
    public ResponseEntity<?> suggestRecipesForExpiringItems(HttpSession session) {
        Long userId = (Long) session.getAttribute("userId");
        if (userId == null) {
            return ResponseEntity.badRequest().body(Map.of(
                "success", false,
                "message", "Please login first"
            ));
        }
        try {
            List<Recipe> recipes = recipeService.suggestRecipesForExpiringItems(userId);
            return ResponseEntity.ok(recipes);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of(
                "success", false,
                "message", e.getMessage()
            ));
        }
    }

    @PostMapping
    public ResponseEntity<?> addRecipe(@RequestBody Recipe recipe, HttpSession session) {
        String role = (String) session.getAttribute("userRole");
        if (!"ADMIN".equals(role)) {
            return ResponseEntity.badRequest().body(Map.of(
                "success", false,
                "message", "Unauthorized. Admin access required."
            ));
        }
        try {
            Recipe newRecipe = recipeService.addRecipe(recipe);
            return ResponseEntity.ok(Map.of(
                "success", true,
                "message", "Recipe added successfully",
                "recipe", newRecipe
            ));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of(
                "success", false,
                "message", e.getMessage()
            ));
        }
    }
}

