package com.recipemanager;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class RecipeRepository {
    private final List<Recipe> recipes = new ArrayList<>();
    private final List<String> categories = new ArrayList<>();

    public RecipeRepository() {
        loadFromDatabase();
    }


    public void addRecipe(Recipe recipe) {
        recipes.add(recipe);
        saveRecipeToDatabase(recipe);

        if (!categories.contains(recipe.getCategory())) {
            categories.add(recipe.getCategory());
        }
    }

    public void removeRecipe(Recipe recipe) {
        recipes.remove(recipe);
        deleteRecipeFromDatabase(recipe.getName());
    }

    public List<Recipe> getAllRecipes() {
        return new ArrayList<>(recipes);
    }

    public List<Recipe> searchRecipes(String query) {
        List<Recipe> result = new ArrayList<>();
        String lowerQuery = query.toLowerCase();
        for (Recipe r : recipes) {
            if (r.getName().toLowerCase().contains(lowerQuery) ||
                    r.getIngredients().toLowerCase().contains(lowerQuery) ||
                    r.getCategory().toLowerCase().contains(lowerQuery)) {
                result.add(r);
            }
        }
        return result;
    }

    public List<Recipe> getRecipesByCategory(String category) {
        List<Recipe> result = new ArrayList<>();
        for (Recipe r : recipes) {
            if (r.getCategory().equalsIgnoreCase(category)) {
                result.add(r);
            }
        }
        return result;
    }

    public List<String> getAllCategories() {
        return new ArrayList<>(categories);
    }

    public void addCategory(String category) {
        if (!categories.contains(category)) {
            categories.add(category);
        }
    }

    // ✅ Stub for legacy call in RecipeApp
    public void loadFromFile(String ignored) {
        loadFromDatabase();
    }

    private void loadFromDatabase() {
        recipes.clear();
        categories.clear();

        String query = "SELECT r.*, c.name AS category_name, d.label AS difficulty_label " +
                "FROM recipes r " +
                "JOIN categories c ON r.category_id = c.id " +
                "JOIN difficulty_levels d ON r.difficulty_id = d.id";

        try (Connection conn = DatabaseManager.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {

            while (rs.next()) {
                Recipe recipe = new Recipe(
                        rs.getString("name"),
                        rs.getString("description"),
                        rs.getString("ingredients"),
                        rs.getString("instructions"),
                        rs.getString("category_name"),
                        rs.getInt("calories"),
                        rs.getInt("servings"),
                        rs.getString("serving_unit"),
                        rs.getInt("prep_time"),
                        rs.getInt("cook_time"),
                        rs.getString("difficulty_label"),
                        rs.getBoolean("is_favorite"),
                        rs.getString("notes"),
                        rs.getDouble("protein"),
                        rs.getDouble("carbs"),
                        rs.getDouble("fat")
                );
                recipe.setRating(rs.getDouble("rating"));
                recipes.add(recipe);

                if (!categories.contains(recipe.getCategory())) {
                    categories.add(recipe.getCategory());
                }
            }

        } catch (SQLException e) {
            System.err.println("❌ Error loading recipes from DB: " + e.getMessage());
        }
    }

    private void saveRecipeToDatabase(Recipe recipe) {
        String insertSQL = "INSERT INTO recipes (" +
                "name, description, ingredients, instructions, category_id, calories, servings, serving_unit, " +
                "prep_time, cook_time, difficulty_id, is_favorite, notes, protein, carbs, fat, rating) " +
                "VALUES (?, ?, ?, ?, " +
                "(SELECT id FROM categories WHERE name = ?), ?, ?, ?, ?, ?, " +
                "(SELECT id FROM difficulty_levels WHERE label = ?), ?, ?, ?, ?, ?, ?)";

        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(insertSQL)) {

            stmt.setString(1, recipe.getName());
            stmt.setString(2, recipe.getDescription());
            stmt.setString(3, recipe.getIngredients());
            stmt.setString(4, recipe.getInstructions());
            stmt.setString(5, recipe.getCategory());
            stmt.setInt(6, recipe.getCalories());
            stmt.setInt(7, recipe.getServings());
            stmt.setString(8, recipe.getServingUnit());
            stmt.setInt(9, recipe.getPrepTime());
            stmt.setInt(10, recipe.getCookTime());
            stmt.setString(11, recipe.getDifficulty());
            stmt.setBoolean(12, recipe.isFavorite());
            stmt.setString(13, recipe.getNotes());
            stmt.setDouble(14, recipe.getProtein());
            stmt.setDouble(15, recipe.getCarbs());
            stmt.setDouble(16, recipe.getFat());
            stmt.setDouble(17, recipe.getRating());

            stmt.executeUpdate();

        } catch (SQLException e) {
            System.err.println("❌ Error saving recipe to DB: " + e.getMessage());
        }
    }

    private void deleteRecipeFromDatabase(String name) {
        String deleteSQL = "DELETE FROM recipes WHERE name = ?";

        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(deleteSQL)) {
            stmt.setString(1, name);
            stmt.executeUpdate();
        } catch (SQLException e) {
            System.err.println("❌ Error deleting recipe from DB: " + e.getMessage());
        }
    }

    // ✅ NEW METHOD: Update an existing recipe by name
    public void updateRecipeInDatabase(Recipe recipe) {
        String updateSQL = "UPDATE recipes SET " +
                "description = ?, ingredients = ?, instructions = ?, " +
                "category_id = (SELECT id FROM categories WHERE name = ?), " +
                "calories = ?, servings = ?, serving_unit = ?, prep_time = ?, cook_time = ?, " +
                "difficulty_id = (SELECT id FROM difficulty_levels WHERE label = ?), " +
                "is_favorite = ?, notes = ?, protein = ?, carbs = ?, fat = ?, rating = ? " +
                "WHERE name = ?";

        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(updateSQL)) {

            stmt.setString(1, recipe.getDescription());
            stmt.setString(2, recipe.getIngredients());
            stmt.setString(3, recipe.getInstructions());
            stmt.setString(4, recipe.getCategory());
            stmt.setInt(5, recipe.getCalories());
            stmt.setInt(6, recipe.getServings());
            stmt.setString(7, recipe.getServingUnit());
            stmt.setInt(8, recipe.getPrepTime());
            stmt.setInt(9, recipe.getCookTime());
            stmt.setString(10, recipe.getDifficulty());
            stmt.setBoolean(11, recipe.isFavorite());
            stmt.setString(12, recipe.getNotes());
            stmt.setDouble(13, recipe.getProtein());
            stmt.setDouble(14, recipe.getCarbs());
            stmt.setDouble(15, recipe.getFat());
            stmt.setDouble(16, recipe.getRating());
            stmt.setString(17, recipe.getName());

            stmt.executeUpdate();

        } catch (SQLException e) {
            System.err.println("❌ Error updating recipe in DB: " + e.getMessage());
        }
    }
}
