package com.recipemanager;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class RecipeApp extends Application {

    private RecipeRepository recipeRepository;
    private RecipeView recipeView;

    @Override
    public void start(Stage primaryStage) {
        // Initialize your repository (database-backed)
        recipeRepository = new RecipeRepository();

        // Initialize your main UI view
        recipeView = new RecipeView(recipeRepository);

        // Setup the main Scene with RecipeView
        Scene scene = new Scene(recipeView.getView(), 1000, 700);

        // Stage configuration
        primaryStage.setTitle("🍲 Recipe Manager");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
