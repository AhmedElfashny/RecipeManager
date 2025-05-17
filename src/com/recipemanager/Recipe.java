package com.recipemanager;

import java.util.ArrayList;
import java.util.List;

public class Recipe {
    private String name;
    private String description;
    private String ingredients;
    private String instructions;
    private String category;
    private int calories;
    private int servings;
    private String servingUnit;
    private int prepTime;
    private int cookTime;
    private String difficulty;
    private List<String> tags;
    private double rating;
    private int servingSize;
    private boolean isFavorite;
    private String notes;
    private double protein;
    private double carbs;
    private double fat;

    // ✅ Constructor used by DB loading (full fields)
    public Recipe(String name, String description, String ingredients, String instructions,
                  String category, int calories, int servings, String servingUnit,
                  int prepTime, int cookTime, String difficulty,
                  boolean isFavorite, String notes,
                  double protein, double carbs, double fat) {
        this.name = name;
        this.description = description;
        this.ingredients = ingredients;
        this.instructions = instructions;
        this.category = category;
        this.calories = calories;
        this.servings = servings;
        this.servingUnit = servingUnit;
        this.prepTime = prepTime;
        this.cookTime = cookTime;
        this.difficulty = difficulty;
        this.tags = new ArrayList<>();
        this.rating = 0.0;
        this.servingSize = servings;
        this.isFavorite = isFavorite;
        this.notes = notes;
        this.protein = protein;
        this.carbs = carbs;
        this.fat = fat;
    }

    // ✅ Backward-compatible minimal constructor
    public Recipe(String name, String description, String ingredients, String instructions,
                  String category, int calories, int servings, String servingUnit) {
        this(name, description, ingredients, instructions, category, calories, servings,
             servingUnit, 15, 20, "Medium", false, "", 0, 0, 0);
    }

    // ✅ Optional intermediate constructor
    public Recipe(String name, String description, String ingredients, String instructions,
                  String category, int calories, int servings, String servingUnit,
                  int prepTime, int cookTime, String difficulty) {
        this(name, description, ingredients, instructions, category, calories, servings,
             servingUnit, prepTime, cookTime, difficulty, false, "", 0, 0, 0);
    }

    // ✅ Utility: full recipe details
    public String getDetails() {
        return "Name: " + name +
               "\nDescription: " + description +
               "\nCategory: " + category +
               "\nDifficulty: " + difficulty +
               "\nPreparation Time: " + prepTime + " minutes" +
               "\nCooking Time: " + cookTime + " minutes" +
               "\nTotal Time: " + (prepTime + cookTime) + " minutes" +
               "\nServing Size: " + servingSize + " people" +
               "\nNutritional Information:" +
               "\n  - Calories per " + servingUnit + ": " + calories +
               "\n  - Total calories: " + getTotalCalories() +
               "\n  - Servings: " + servings + " " + servingUnit + (servings > 1 ? "s" : "") +
               "\nRating: " + String.format("%.1f", rating) + "/5.0" +
               "\nTags: " + String.join(", ", tags) +
               "\nIngredients:\n" + ingredients +
               "\nInstructions:\n" + instructions;
    }

    // ✅ Utility methods
    public void addTag(String tag) {
        if (!tags.contains(tag.toLowerCase())) {
            tags.add(tag.toLowerCase());
        }
    }

    public int getTotalCalories() {
        return calories * servings;
    }

    public String getCaloriesPerServing() {
        return calories + " calories per " + servingUnit;
    }

    // ✅ Getters / Setters
    public String getName() { return name; }
    public String getDescription() { return description; }
    public String getIngredients() { return ingredients; }
    public String getInstructions() { return instructions; }
    public String getCategory() { return category; }
    public int getCalories() { return calories; }
    public int getServings() { return servings; }
    public String getServingUnit() { return servingUnit; }
    public int getPrepTime() { return prepTime; }
    public int getCookTime() { return cookTime; }
    public String getDifficulty() { return difficulty; }
    public List<String> getTags() { return new ArrayList<>(tags); }
    public double getRating() { return rating; }
    public void setRating(double rating) {
        if (rating >= 0 && rating <= 5) {
            this.rating = rating;
        }
    }

    public int getServingSize() { return servingSize; }

    public boolean isFavorite() { return isFavorite; }
    public void setFavorite(boolean favorite) { this.isFavorite = favorite; }

    public String getNotes() { return notes; }
    public void setNotes(String notes) { this.notes = notes; }

    public double getProtein() { return protein; }
    public void setProtein(double protein) { this.protein = protein; }

    public double getCarbs() { return carbs; }
    public void setCarbs(double carbs) { this.carbs = carbs; }

    public double getFat() { return fat; }
    public void setFat(double fat) { this.fat = fat; }
}
