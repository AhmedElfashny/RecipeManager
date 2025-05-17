package com.recipemanager;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.geometry.Side;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.control.TextArea;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class RecipeView {

    private final VBox root;
    private final ListView<String> recipeList;
    private final TextField searchField;
    private final VBox detailsBox;
    private final ContextMenu autoCompleteMenu;
    private final RecipeRepository recipeRepository;

    public RecipeView(RecipeRepository recipeRepository) {
        this.recipeRepository = recipeRepository;

        root = new VBox(10);
        root.setPadding(new Insets(20));
        root.setStyle("-fx-background-color: linear-gradient(to bottom right, #fffbe0, #ffe0b2);");

        recipeList = new ListView<>();
        searchField = new TextField();
        detailsBox = new VBox(10);
        autoCompleteMenu = new ContextMenu();

        root.getChildren().addAll(
            createTitle(),
            createSearchBar(),
            createRecipeList(),
            createDetailsPane(),
            createButtonBar()
        );

        updateRecipeList();
    }

    private Label createTitle() {
        Label titleLabel = new Label("🍲 Recipe Manager");
        titleLabel.setStyle("-fx-font-size: 32px; -fx-font-weight: bold; -fx-text-fill: #d35400;");
        titleLabel.setAlignment(Pos.CENTER);
        titleLabel.setMaxWidth(Double.MAX_VALUE);
        return titleLabel;
    }

    private HBox createSearchBar() {
        HBox searchBox = new HBox(10);
        searchBox.setAlignment(Pos.CENTER);

        searchField.setPromptText("Enter ingredients or recipe name...");
        searchField.setPrefWidth(400);
        searchField.setOnKeyReleased(e -> handleAutoComplete());

        Button searchButton = new Button("Search 🍴");
        searchButton.setOnAction(e -> searchRecipes());

        searchBox.getChildren().addAll(searchField, searchButton);
        return searchBox;
    }

    private ListView<String> createRecipeList() {
        recipeList.setPrefHeight(300);
        recipeList.getSelectionModel().selectedItemProperty()
                .addListener((obs, oldVal, newVal) -> showRecipeDetails(newVal));
        return recipeList;
    }

    private ScrollPane createDetailsPane() {
        detailsBox.setPadding(new Insets(20));
        ScrollPane scrollPane = new ScrollPane(detailsBox);
        scrollPane.setFitToWidth(true);
        scrollPane.setPrefHeight(350);
        return scrollPane;
    }

    private HBox createButtonBar() {
        HBox buttonBox = new HBox(10);
        buttonBox.setAlignment(Pos.CENTER);

        Button addButton = new Button("Add Recipe ➕");
        addButton.setOnAction(e -> addRecipe());

        Button removeButton = new Button("Remove Recipe ❌");
        removeButton.setOnAction(e -> removeSelectedRecipe());

        buttonBox.getChildren().addAll(addButton, removeButton);
        return buttonBox;
    }

    public VBox getView() {
        return root;
    }

    public void updateRecipeList() {
        List<String> recipeNames = recipeRepository.getAllRecipes().stream()
            .map(Recipe::getName)
            .collect(Collectors.toList());
        recipeList.getItems().setAll(recipeNames);
    }

    private void showRecipeDetails(String recipeName) {
        detailsBox.getChildren().clear();
        if (recipeName == null) return;

        Recipe recipe = recipeRepository.getAllRecipes().stream()
                .filter(r -> r.getName().equals(recipeName))
                .findFirst().orElse(null);

        if (recipe == null) return;

        Label nameLabel = new Label(recipe.getName());
        nameLabel.setStyle("-fx-font-size: 22px; -fx-font-weight: bold;");

        Label descLabel = new Label(recipe.getDescription());
        Label catLabel = new Label("Category: " + recipe.getCategory());
        Label diffLabel = new Label("Difficulty: " + recipe.getDifficulty());
        Label timeLabel = new Label("Prep: " + recipe.getPrepTime() + " min | Cook: " + recipe.getCookTime() + " min");

        TextArea ingArea = new TextArea(recipe.getIngredients());
        ingArea.setEditable(false);
        ingArea.setWrapText(true);
        ingArea.setPrefRowCount(4);

        TextArea instArea = new TextArea(recipe.getInstructions());
        instArea.setEditable(false);
        instArea.setWrapText(true);
        instArea.setPrefRowCount(6);

        detailsBox.getChildren().addAll(
                nameLabel, descLabel, catLabel, diffLabel, timeLabel,
                new Label("🥄 Ingredients:"), ingArea,
                new Label("📋 Instructions:"), instArea
        );
    }

    private void handleAutoComplete() {
        String text = searchField.getText().toLowerCase();
        autoCompleteMenu.hide();
        if (text.isEmpty()) return;

        List<String> suggestions = recipeRepository.getAllRecipes().stream()
                .flatMap(r -> Arrays.stream((r.getName() + "," + r.getIngredients()).split(",")))
                .map(String::trim)
                .filter(s -> s.toLowerCase().contains(text))
                .distinct()
                .limit(10)
                .collect(Collectors.toList());

        if (suggestions.isEmpty()) return;

        autoCompleteMenu.getItems().setAll(suggestions.stream().map(s -> {
            CustomMenuItem item = new CustomMenuItem(new Label(s), true);
            item.setOnAction(e -> {
                searchField.setText(s);
                searchRecipes();
            });
            return item;
        }).collect(Collectors.toList()));

        autoCompleteMenu.show(searchField, Side.BOTTOM, 0, 0);
    }

    private void searchRecipes() {
        String query = searchField.getText().trim().toLowerCase();
        List<Recipe> results = query.isEmpty()
            ? recipeRepository.getAllRecipes()
            : recipeRepository.searchRecipes(query);
        recipeList.getItems().setAll(results.stream().map(Recipe::getName).collect(Collectors.toList()));
    }

    private void addRecipe() {
        Dialog<String> dialog = new TextInputDialog();
        dialog.setTitle("Add New Recipe");
        dialog.setHeaderText("Enter Recipe Name:");
        dialog.setContentText("Recipe Name:");

        dialog.showAndWait().ifPresent(name -> {
            Recipe newRecipe = new Recipe(
                name, "New description", "", "", "Uncategorized", 0, 1, "serving"
            );
            recipeRepository.addRecipe(newRecipe);
            updateRecipeList();
        });
    }

    private void removeSelectedRecipe() {
        String selected = recipeList.getSelectionModel().getSelectedItem();
        if (selected == null) return;

        Recipe recipeToRemove = recipeRepository.getAllRecipes().stream()
                .filter(r -> r.getName().equals(selected))
                .findFirst().orElse(null);

        if (recipeToRemove != null) {
            recipeRepository.removeRecipe(recipeToRemove);
            updateRecipeList();
            detailsBox.getChildren().clear();
        }
    }
}
