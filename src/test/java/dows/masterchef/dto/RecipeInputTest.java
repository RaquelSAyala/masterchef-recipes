package dows.masterchef.dto;


import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import dows.masterchef.model.AuthorType;

import java.util.Arrays;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class RecipeInputTest {

    private Validator validator;
    private RecipeInput recipeInput;

    @BeforeEach
    void setUp() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
        recipeInput = new RecipeInput();
    }

    @Test
    void createRecipeInput_WithValidData_ShouldPassValidation() {
        // Arrange
        recipeInput.setTitle("Test Recipe");
        recipeInput.setIngredients(Arrays.asList("Ingredient 1", "Ingredient 2"));
        recipeInput.setSteps(Arrays.asList("Step 1", "Step 2"));
        recipeInput.setChefName("Test Chef");
        recipeInput.setAuthorType(AuthorType.CHEF);
        recipeInput.setSeason(Integer.valueOf(1));

        // Act
        Set<ConstraintViolation<RecipeInput>> violations = validator.validate(recipeInput);

        // Assert
        assertTrue(violations.isEmpty(), "Should have no validation violations");
    }

    @Test
    void title_WhenBlank_ShouldFailValidation() {
        // Arrange
        recipeInput.setTitle("   "); // Blank title
        recipeInput.setIngredients(Arrays.asList("Ingredient 1"));
        recipeInput.setSteps(Arrays.asList("Step 1"));
        recipeInput.setChefName("Test Chef");
        recipeInput.setAuthorType(AuthorType.CHEF);

        // Act
        Set<ConstraintViolation<RecipeInput>> violations = validator.validate(recipeInput);

        // Assert
        assertFalse(violations.isEmpty());
        assertEquals(1, violations.size());
        assertEquals("title", violations.iterator().next().getPropertyPath().toString());
    }

    @Test
    void title_WhenNull_ShouldFailValidation() {
        // Arrange
        recipeInput.setTitle(null); // Null title
        recipeInput.setIngredients(Arrays.asList("Ingredient 1"));
        recipeInput.setSteps(Arrays.asList("Step 1"));
        recipeInput.setChefName("Test Chef");
        recipeInput.setAuthorType(AuthorType.CHEF);

        // Act
        Set<ConstraintViolation<RecipeInput>> violations = validator.validate(recipeInput);

        // Assert
        assertFalse(violations.isEmpty());
        assertTrue(violations.stream()
                .anyMatch(v -> v.getPropertyPath().toString().equals("title")));
    }

    @Test
    void ingredients_WhenEmpty_ShouldFailValidation() {
        // Arrange
        recipeInput.setTitle("Test Recipe");
        recipeInput.setIngredients(Arrays.asList()); // Empty list
        recipeInput.setSteps(Arrays.asList("Step 1"));
        recipeInput.setChefName("Test Chef");
        recipeInput.setAuthorType(AuthorType.CHEF);

        // Act
        Set<ConstraintViolation<RecipeInput>> violations = validator.validate(recipeInput);

        // Assert
        assertFalse(violations.isEmpty());
        assertTrue(violations.stream()
                .anyMatch(v -> v.getPropertyPath().toString().equals("ingredients")));
    }

    @Test
    void ingredients_WhenNull_ShouldFailValidation() {
        // Arrange
        recipeInput.setTitle("Test Recipe");
        recipeInput.setIngredients(null); // Null list
        recipeInput.setSteps(Arrays.asList("Step 1"));
        recipeInput.setChefName("Test Chef");
        recipeInput.setAuthorType(AuthorType.CHEF);

        // Act
        Set<ConstraintViolation<RecipeInput>> violations = validator.validate(recipeInput);

        // Assert
        assertFalse(violations.isEmpty());
        assertTrue(violations.stream()
                .anyMatch(v -> v.getPropertyPath().toString().equals("ingredients")));
    }

    @Test
    void ingredients_WhenContainsBlankStrings_ShouldFailValidation() {
        // Arrange
        recipeInput.setTitle("Test Recipe");
        recipeInput.setIngredients(Arrays.asList("Valid Ingredient", "   ")); // Contains blank
        recipeInput.setSteps(Arrays.asList("Step 1"));
        recipeInput.setChefName("Test Chef");
        recipeInput.setAuthorType(AuthorType.CHEF);

        // Act
        Set<ConstraintViolation<RecipeInput>> violations = validator.validate(recipeInput);

        // Assert
        assertFalse(violations.isEmpty());
        // The violation might be on the list elements rather than the list itself
        assertTrue(violations.stream()
                .anyMatch(v -> v.getPropertyPath().toString().contains("ingredients")));
    }

    @Test
    void steps_WhenEmpty_ShouldFailValidation() {
        // Arrange
        recipeInput.setTitle("Test Recipe");
        recipeInput.setIngredients(Arrays.asList("Ingredient 1"));
        recipeInput.setSteps(Arrays.asList()); // Empty list
        recipeInput.setChefName("Test Chef");
        recipeInput.setAuthorType(AuthorType.CHEF);

        // Act
        Set<ConstraintViolation<RecipeInput>> violations = validator.validate(recipeInput);

        // Assert
        assertFalse(violations.isEmpty());
        assertTrue(violations.stream()
                .anyMatch(v -> v.getPropertyPath().toString().equals("steps")));
    }

    @Test
    void steps_WhenContainsBlankStrings_ShouldFailValidation() {
        // Arrange
        recipeInput.setTitle("Test Recipe");
        recipeInput.setIngredients(Arrays.asList("Ingredient 1"));
        recipeInput.setSteps(Arrays.asList("Valid Step", "")); // Contains blank
        recipeInput.setChefName("Test Chef");
        recipeInput.setAuthorType(AuthorType.CHEF);

        // Act
        Set<ConstraintViolation<RecipeInput>> violations = validator.validate(recipeInput);

        // Assert
        assertFalse(violations.isEmpty());
        assertTrue(violations.stream()
                .anyMatch(v -> v.getPropertyPath().toString().contains("steps")));
    }

    @Test
    void chefName_WhenBlank_ShouldFailValidation() {
        // Arrange
        recipeInput.setTitle("Test Recipe");
        recipeInput.setIngredients(Arrays.asList("Ingredient 1"));
        recipeInput.setSteps(Arrays.asList("Step 1"));
        recipeInput.setChefName("   "); // Blank chef name
        recipeInput.setAuthorType(AuthorType.CHEF);

        // Act
        Set<ConstraintViolation<RecipeInput>> violations = validator.validate(recipeInput);

        // Assert
        assertFalse(violations.isEmpty());
        assertEquals(1, violations.size());
        assertEquals("chefName", violations.iterator().next().getPropertyPath().toString());
    }

    @Test
    void authorType_WhenNull_ShouldFailValidation() {
        // Arrange
        recipeInput.setTitle("Test Recipe");
        recipeInput.setIngredients(Arrays.asList("Ingredient 1"));
        recipeInput.setSteps(Arrays.asList("Step 1"));
        recipeInput.setChefName("Test Chef");
        recipeInput.setAuthorType(null); // Null author type

        // Act
        Set<ConstraintViolation<RecipeInput>> violations = validator.validate(recipeInput);

        // Assert
        assertFalse(violations.isEmpty());
        assertTrue(violations.stream()
                .anyMatch(v -> v.getPropertyPath().toString().equals("authorType")));
    }

    @Test
    void season_WhenNull_ShouldPassValidation() {
        // Arrange
        recipeInput.setTitle("Test Recipe");
        recipeInput.setIngredients(Arrays.asList("Ingredient 1"));
        recipeInput.setSteps(Arrays.asList("Step 1"));
        recipeInput.setChefName("Test Chef");
        recipeInput.setAuthorType(AuthorType.CHEF);
        recipeInput.setSeason(null); // Null season is allowed

        // Act
        Set<ConstraintViolation<RecipeInput>> violations = validator.validate(recipeInput);

        // Assert
        assertTrue(violations.isEmpty(), "Season can be null");
    }

    @Test
    void gettersAndSetters_ShouldWorkCorrectly() {
        // Arrange
        String title = "Test Recipe";
        List<String> ingredients = Arrays.asList("Ingredient 1", "Ingredient 2");
        List<String> steps = Arrays.asList("Step 1", "Step 2");
        String chefName = "Test Chef";
        AuthorType authorType = AuthorType.PARTICIPANT;
        Integer season = (Integer) 5;

        // Act
        recipeInput.setTitle(title);
        recipeInput.setIngredients(ingredients);
        recipeInput.setSteps(steps);
        recipeInput.setChefName(chefName);
        recipeInput.setAuthorType(authorType);
        recipeInput.setSeason(season);

        // Assert
        assertEquals(title, recipeInput.getTitle());
        assertEquals(ingredients, recipeInput.getIngredients());
        assertEquals(steps, recipeInput.getSteps());
        assertEquals(chefName, recipeInput.getChefName());
        assertEquals(authorType, recipeInput.getAuthorType());
        assertEquals(season, recipeInput.getSeason());
    }

    @Test
    void createRecipeInput_WithAllAuthorTypes_ShouldPassValidation() {
        // Test all possible AuthorType values
        for (AuthorType authorType : AuthorType.values()) {
            // Arrange
            RecipeInput input = new RecipeInput();
            input.setTitle("Test Recipe");
            input.setIngredients(Arrays.asList("Ingredient"));
            input.setSteps(Arrays.asList("Step"));
            input.setChefName("Chef");
            input.setAuthorType(authorType);

            // Act
            Set<ConstraintViolation<RecipeInput>> violations = validator.validate(input);

            // Assert
            assertTrue(violations.isEmpty(),
                    "Validation should pass for author type: " + authorType);
        }
    }

    @Test
    void createRecipeInput_WithValidSeasonValues_ShouldPassValidation() {
        // Test various valid season values
        Integer[] validSeasons = {(Integer) 1, (Integer) 5, (Integer) 10, null};

        for (Integer season : validSeasons) {
            // Arrange
            RecipeInput input = new RecipeInput();
            input.setTitle("Test Recipe");
            input.setIngredients(Arrays.asList("Ingredient"));
            input.setSteps(Arrays.asList("Step"));
            input.setChefName("Chef");
            input.setAuthorType(AuthorType.VIEWER);
            input.setSeason(season);

            // Act
            Set<ConstraintViolation<RecipeInput>> violations = validator.validate(input);

            // Assert
            assertTrue(violations.isEmpty(),
                    "Validation should pass for season: " + season);
        }
    }

    @Test
    void createRecipeInput_WithMinimumValidData_ShouldPassValidation() {
        // Arrange - minimum required fields
        RecipeInput input = new RecipeInput();
        input.setTitle("T"); // Minimum non-blank title
        input.setIngredients(Arrays.asList("I")); // Single non-blank ingredient
        input.setSteps(Arrays.asList("S")); // Single non-blank step
        input.setChefName("C"); // Minimum non-blank chef name
        input.setAuthorType(AuthorType.CHEF);
        // Season is optional

        // Act
        Set<ConstraintViolation<RecipeInput>> violations = validator.validate(input);

        // Assert
        assertTrue(violations.isEmpty(),
                "Validation should pass with minimum valid data");
    }
}