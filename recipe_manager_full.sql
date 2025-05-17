
-- Drop and recreate the database
DROP DATABASE IF EXISTS recipe_manager;
CREATE DATABASE recipe_manager;
USE recipe_manager;

-- Table: categories
CREATE TABLE categories (
    id INT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(100) NOT NULL UNIQUE
);

-- Table: difficulty_levels
CREATE TABLE difficulty_levels (
    id INT AUTO_INCREMENT PRIMARY KEY,
    label VARCHAR(50) NOT NULL UNIQUE
);

-- Table: recipes (without image_path)
CREATE TABLE recipes (
    id INT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(255) NOT NULL UNIQUE,
    description TEXT,
    ingredients TEXT,
    instructions TEXT,
    category_id INT,
    calories INT,
    servings INT,
    serving_unit VARCHAR(50),
    prep_time INT,
    cook_time INT,
    difficulty_id INT,
    is_favorite BOOLEAN DEFAULT FALSE,
    notes TEXT,
    protein DOUBLE,
    carbs DOUBLE,
    fat DOUBLE,
    rating DOUBLE DEFAULT 0.0,
    FOREIGN KEY (category_id) REFERENCES categories(id),
    FOREIGN KEY (difficulty_id) REFERENCES difficulty_levels(id)
);

-- Table: tags
CREATE TABLE tags (
    id INT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(100) NOT NULL UNIQUE
);

-- Table: recipe_tags
CREATE TABLE recipe_tags (
    recipe_id INT NOT NULL,
    tag_id INT NOT NULL,
    PRIMARY KEY (recipe_id, tag_id),
    FOREIGN KEY (recipe_id) REFERENCES recipes(id) ON DELETE CASCADE,
    FOREIGN KEY (tag_id) REFERENCES tags(id) ON DELETE CASCADE
);

-- Insert difficulty levels
INSERT INTO difficulty_levels (label) VALUES 
('Easy'), ('Medium'), ('Hard');

-- Insert categories (from Java code)
INSERT INTO categories (name) VALUES
('Breakfast'), ('Lunch'), ('Dinner'), ('Appetizers'), ('Soups & Stews'),
('Main Dishes'), ('Side Dishes'), ('Salads'), ('Desserts'), ('Snacks'),
('Beverages'), ('Baked Goods'), ('Vegetarian'), ('Vegan'), ('Gluten-Free'),
('Low-Carb'), ('Keto'), ('Mediterranean'), ('Asian Cuisine'),
('Italian Cuisine'), ('Mexican Cuisine'), ('Quick & Easy'),
('Slow Cooker'), ('Holiday Specials'), ('Other');

-- Insert multiple recipes
INSERT INTO recipes (name, description, ingredients, instructions, category_id, calories, servings, serving_unit, prep_time, cook_time, difficulty_id, is_favorite, notes, protein, carbs, fat, rating)
VALUES 
-- Classic Pancakes
('Classic Pancakes',
 'Fluffy and delicious breakfast pancakes',
 '2 cups flour\n2 tbsp sugar\n2 tsp baking powder\n1/2 tsp salt\n2 eggs\n2 cups milk\n2 tbsp butter',
 '1. Mix dry ingredients\n2. Whisk eggs, milk, and melted butter\n3. Combine wet and dry ingredients\n4. Cook on griddle until golden',
 (SELECT id FROM categories WHERE name = 'Breakfast'), 250, 4, 'pancake', 15, 20,
 (SELECT id FROM difficulty_levels WHERE label = 'Easy'), FALSE, '', 10, 48, 10, 0.0),

-- Eggs Benedict
('Eggs Benedict',
 'Classic brunch favorite with hollandaise sauce',
 '4 English muffins\n8 eggs\n8 slices Canadian bacon\n1 cup butter\n4 egg yolks\n1 tbsp lemon juice\nSalt and pepper to taste',
 '1. Prepare hollandaise sauce\n2. Toast English muffins\n3. Poach eggs\n4. Cook Canadian bacon\n5. Assemble and serve',
 (SELECT id FROM categories WHERE name = 'Breakfast'), 450, 4, 'serving', 25, 20,
 (SELECT id FROM difficulty_levels WHERE label = 'Medium'), FALSE, '', 20, 48, 20, 0.0),

-- Omelette
('Omelette',
 'Simple and customizable breakfast omelette',
 '3 eggs\n1/4 cup milk\nSalt and pepper\n1/4 cup cheese\nOptional: diced vegetables, ham, herbs',
 '1. Beat eggs with milk, salt, and pepper\n2. Pour into pan\n3. Add fillings\n4. Fold and serve',
 (SELECT id FROM categories WHERE name = 'Breakfast'), 220, 1, 'omelette', 5, 5,
 (SELECT id FROM difficulty_levels WHERE label = 'Easy'), FALSE, '', 4, 4, 1, 0.0),

-- Greek Salad
('Greek Salad',
 'Fresh and healthy Mediterranean salad',
 '1 cucumber\n4 tomatoes\n1 red onion\n200g feta cheese\nKalamata olives\nolive oil\noregano',
 '1. Chop vegetables\n2. Combine in bowl\n3. Add feta and olives\n4. Dress with olive oil and oregano',
 (SELECT id FROM categories WHERE name = 'Lunch'), 180, 2, 'bowl', 10, 0,
 (SELECT id FROM difficulty_levels WHERE label = 'Easy'), FALSE, '', 2, 10, 0, 0.0),

-- Chicken Caesar Wrap
('Chicken Caesar Wrap',
 'Classic Caesar salad in a convenient wrap',
 '4 tortillas\n2 chicken breasts\nRomaine lettuce\nParmesan cheese\nCaesar dressing\nCroutons',
 '1. Grill chicken and slice\n2. Warm tortillas\n3. Assemble wraps with ingredients\n4. Roll and serve',
 (SELECT id FROM categories WHERE name = 'Lunch'), 320, 4, 'wrap', 15, 15,
 (SELECT id FROM difficulty_levels WHERE label = 'Easy'), FALSE, '', 10, 20, 10, 0.0);


CREATE USER 'recipe_app_user'@'localhost' IDENTIFIED BY 'strongpassword123';

GRANT ALL PRIVILEGES ON recipe_manager.* TO 'recipe_app_user'@'localhost';

FLUSH PRIVILEGES;