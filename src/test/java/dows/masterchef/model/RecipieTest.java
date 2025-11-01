package dows.masterchef.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import java.lang.reflect.Field;
import java.time.Instant;
import java.util.Arrays;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class RecipeTest {

    private Recipe recipe;
    private Validator validator;

    @BeforeEach
    void setUp() {
        recipe = new Recipe();
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @Test
    void defaultConstructor_ShouldCreateEmptyRecipe() {
        assertNotNull(recipe);
        assertNull(recipe.getId());
        assertNull(recipe.getSeq());
        assertNull(recipe.getTitle());
        assertNull(recipe.getIngredients());
        assertNull(recipe.getSteps());
        assertNull(recipe.getChefName());
        assertNull(recipe.getAuthorType());
        assertNull(recipe.getSeason());
        assertNull(recipe.getCreatedAt());
        assertNull(recipe.getUpdatedAt());
    }

    @Test
    void settersAndGetters_ShouldWorkCorrectly() {
        // Arrange
        String id = "507f1f77bcf86cd799439011";
        Long seq = (Long) 1L;
        String title = "Test Recipe";
        List<String> ingredients = Arrays.asList("Ingredient 1", "Ingredient 2");
        List<String> steps = Arrays.asList("Step 1", "Step 2");
        String chefName = "Test Chef";
        AuthorType authorType = AuthorType.CHEF;
        Integer season = (Integer) 5;
        Instant createdAt = Instant.now();
        Instant updatedAt = Instant.now().plusSeconds(60);

        // Act
        recipe.setId(id);
        recipe.setSeq(seq);
        recipe.setTitle(title);
        recipe.setIngredients(ingredients);
        recipe.setSteps(steps);
        recipe.setChefName(chefName);
        recipe.setAuthorType(authorType);
        recipe.setSeason(season);
        recipe.setCreatedAt(createdAt);
        recipe.setUpdatedAt(updatedAt);

        // Assert
        assertEquals(id, recipe.getId());
        assertEquals(seq, recipe.getSeq());
        assertEquals(title, recipe.getTitle());
        assertEquals(ingredients, recipe.getIngredients());
        assertEquals(steps, recipe.getSteps());
        assertEquals(chefName, recipe.getChefName());
        assertEquals(authorType, recipe.getAuthorType());
        assertEquals(season, recipe.getSeason());
        assertEquals(createdAt, recipe.getCreatedAt());
        assertEquals(updatedAt, recipe.getUpdatedAt());
    }

    @Test
    void class_ShouldHaveDocumentAnnotation() {
        Document documentAnnotation = Recipe.class.getAnnotation(Document.class);
        assertNotNull(documentAnnotation);
        assertEquals("recipes", documentAnnotation.collection());
    }

    @Test
    void idField_ShouldHaveIdAnnotation() throws NoSuchFieldException {
        Field idField = Recipe.class.getDeclaredField("id");
        assertNotNull(idField.getAnnotation(Id.class));
    }

    @Test
    void seqField_ShouldHaveIndexedAnnotation() throws NoSuchFieldException {
        Field seqField = Recipe.class.getDeclaredField("seq");
        Indexed indexedAnnotation = seqField.getAnnotation(Indexed.class);
        assertNotNull(indexedAnnotation);
        assertTrue(indexedAnnotation.unique());
    }

    @Test
    void createdAtField_ShouldHaveCreatedDateAnnotation() throws NoSuchFieldException {
        Field createdAtField = Recipe.class.getDeclaredField("createdAt");
        assertNotNull(createdAtField.getAnnotation(CreatedDate.class));
    }

    @Test
    void updatedAtField_ShouldHaveLastModifiedDateAnnotation() throws NoSuchFieldException {
        Field updatedAtField = Recipe.class.getDeclaredField("updatedAt");
        assertNotNull(updatedAtField.getAnnotation(LastModifiedDate.class));
    }

    // Validation Tests
    @Test
    void validRecipe_ShouldPassValidation() {
        // Arrange
        Recipe validRecipe = createValidRecipe();

        // Act
        Set<ConstraintViolation<Recipe>> violations = validator.validate(validRecipe);

        // Assert
        assertTrue(violations.isEmpty(), "Should have no validation violations");
    }

    @Test
    void title_WhenBlank_ShouldFailValidation() {
        // Arrange
        Recipe invalidRecipe = createValidRecipe();
        invalidRecipe.setTitle("   ");

        // Act
        Set<ConstraintViolation<Recipe>> violations = validator.validate(invalidRecipe);

        // Assert
        assertFalse(violations.isEmpty());
        assertTrue(violations.stream()
                .anyMatch(v -> v.getPropertyPath().toString().equals("title")));
    }

    @Test
    void title_WhenNull_ShouldFailValidation() {
        // Arrange
        Recipe invalidRecipe = createValidRecipe();
        invalidRecipe.setTitle(null);

        // Act
        Set<ConstraintViolation<Recipe>> violations = validator.validate(invalidRecipe);

        // Assert
        assertFalse(violations.isEmpty());
        assertTrue(violations.stream()
                .anyMatch(v -> v.getPropertyPath().toString().equals("title")));
    }

    @Test
    void ingredients_WhenEmpty_ShouldFailValidation() {
        // Arrange
        Recipe invalidRecipe = createValidRecipe();
        invalidRecipe.setIngredients(Arrays.asList());

        // Act
        Set<ConstraintViolation<Recipe>> violations = validator.validate(invalidRecipe);

        // Assert
        assertFalse(violations.isEmpty());
        assertTrue(violations.stream()
                .anyMatch(v -> v.getPropertyPath().toString().equals("ingredients")));
    }

    @Test
    void ingredients_WhenNull_ShouldFailValidation() {
        // Arrange
        Recipe invalidRecipe = createValidRecipe();
        invalidRecipe.setIngredients(null);

        // Act
        Set<ConstraintViolation<Recipe>> violations = validator.validate(invalidRecipe);

        // Assert
        assertFalse(violations.isEmpty());
        assertTrue(violations.stream()
                .anyMatch(v -> v.getPropertyPath().toString().equals("ingredients")));
    }

    @Test
    void ingredients_WhenContainsBlankStrings_ShouldFailValidation() {
        // Arrange
        Recipe invalidRecipe = createValidRecipe();
        invalidRecipe.setIngredients(Arrays.asList("Valid Ingredient", "   "));

        // Act
        Set<ConstraintViolation<Recipe>> violations = validator.validate(invalidRecipe);

        // Assert
        assertFalse(violations.isEmpty());
    }

    @Test
    void steps_WhenEmpty_ShouldFailValidation() {
        // Arrange
        Recipe invalidRecipe = createValidRecipe();
        invalidRecipe.setSteps(Arrays.asList());

        // Act
        Set<ConstraintViolation<Recipe>> violations = validator.validate(invalidRecipe);

        // Assert
        assertFalse(violations.isEmpty());
        assertTrue(violations.stream()
                .anyMatch(v -> v.getPropertyPath().toString().equals("steps")));
    }

    @Test
    void steps_WhenContainsBlankStrings_ShouldFailValidation() {
        // Arrange
        Recipe invalidRecipe = createValidRecipe();
        invalidRecipe.setSteps(Arrays.asList("Valid Step", ""));

        // Act
        Set<ConstraintViolation<Recipe>> violations = validator.validate(invalidRecipe);

        // Assert
        assertFalse(violations.isEmpty());
    }

    @Test
    void chefName_WhenBlank_ShouldFailValidation() {
        // Arrange
        Recipe invalidRecipe = createValidRecipe();
        invalidRecipe.setChefName("   ");

        // Act
        Set<ConstraintViolation<Recipe>> violations = validator.validate(invalidRecipe);

        // Assert
        assertFalse(violations.isEmpty());
        assertTrue(violations.stream()
                .anyMatch(v -> v.getPropertyPath().toString().equals("chefName")));
    }

    @Test
    void authorType_WhenNull_ShouldFailValidation() {
        // Arrange
        Recipe invalidRecipe = createValidRecipe();
        invalidRecipe.setAuthorType(null);

        // Act
        Set<ConstraintViolation<Recipe>> violations = validator.validate(invalidRecipe);

        // Assert
        assertFalse(violations.isEmpty());
        assertTrue(violations.stream()
                .anyMatch(v -> v.getPropertyPath().toString().equals("authorType")));
    }

    @Test
    void season_WhenNull_ShouldPassValidation() {
        // Arrange
        Recipe validRecipe = createValidRecipe();
        validRecipe.setSeason(null);

        // Act
        Set<ConstraintViolation<Recipe>> violations = validator.validate(validRecipe);

        // Assert
        assertTrue(violations.isEmpty(), "Season can be null");
    }

    @Test
    void season_WithNegativeValue_ShouldPassValidation() {
        // Arrange
        Recipe validRecipe = createValidRecipe();
        validRecipe.setSeason(Integer.valueOf(-1));

        // Act
        Set<ConstraintViolation<Recipe>> violations = validator.validate(validRecipe);

        // Assert
        assertTrue(violations.isEmpty(), "Season can be negative");
    }

    @Test
    void season_WithZero_ShouldPassValidation() {
        // Arrange
        Recipe validRecipe = createValidRecipe();
        validRecipe.setSeason(Integer.valueOf(0));

        // Act
        Set<ConstraintViolation<Recipe>> violations = validator.validate(validRecipe);

        // Assert
        assertTrue(violations.isEmpty(), "Season can be zero");
    }

    // Edge Cases and Special Scenarios
    @Test
    void recipe_WithAllAuthorTypes_ShouldPassValidation() {
        for (AuthorType authorType : AuthorType.values()) {
            // Arrange
            Recipe recipe = createValidRecipe();
            recipe.setAuthorType(authorType);

            // Act
            Set<ConstraintViolation<Recipe>> violations = validator.validate(recipe);

            // Assert
            assertTrue(violations.isEmpty(),
                    "Should pass validation for author type: " + authorType);
        }
    }

    @Test
    void recipe_WithLongStrings_ShouldPassValidation() {
        // Arrange
        Recipe recipe = createValidRecipe();
        recipe.setTitle("A".repeat(1000));
        recipe.setChefName("B".repeat(100));
        recipe.setIngredients(Arrays.asList("C".repeat(500)));
        recipe.setSteps(Arrays.asList("D".repeat(1000)));

        // Act
        Set<ConstraintViolation<Recipe>> violations = validator.validate(recipe);

        // Assert
        assertTrue(violations.isEmpty(), "Should handle long strings");
    }

    @Test
    void timestamps_ShouldBeMutable() {
        // Arrange
        Instant initialCreatedAt = Instant.now().minusSeconds(3600);
        Instant initialUpdatedAt = Instant.now().minusSeconds(1800);
        Instant newCreatedAt = Instant.now();
        Instant newUpdatedAt = Instant.now().plusSeconds(60);

        // Act
        recipe.setCreatedAt(initialCreatedAt);
        recipe.setUpdatedAt(initialUpdatedAt);
        recipe.setCreatedAt(newCreatedAt);
        recipe.setUpdatedAt(newUpdatedAt);

        // Assert
        assertEquals(newCreatedAt, recipe.getCreatedAt());
        assertEquals(newUpdatedAt, recipe.getUpdatedAt());
    }

    @Test
    void seq_WithVariousValues_ShouldBeHandledCorrectly() {
        Long[] testValues = {null, (Long) 0L, (Long) 1L, (Long) 4L, (Long) Long.MAX_VALUE, (Long) Long.MIN_VALUE};

        for (Long testValue : testValues) {
            // Arrange & Act
            recipe.setSeq(testValue);

            // Assert
            assertEquals(testValue, recipe.getSeq(),
                    "Should handle seq value: " + testValue);
        }
    }

    @Test
    void equals_WithSameObject_ShouldReturnTrue() {
        assertEquals(recipe, recipe);
    }

    @Test
    void equals_WithDifferentType_ShouldReturnFalse() {
        assertNotEquals(recipe, "not a recipe");
    }

    @Test
    void equals_WithNull_ShouldReturnFalse() {
        assertNotEquals(recipe, null);
    }

    @Test
    void hashCode_Consistency_ShouldReturnSameHashCode() {
        int firstHashCode = recipe.hashCode();
        int secondHashCode = recipe.hashCode();
        assertEquals(firstHashCode, secondHashCode);
    }

    @Test
    void toString_ShouldReturnNonEmptyString() {
        String toStringResult = recipe.toString();
        assertNotNull(toStringResult);
        assertFalse(toStringResult.isEmpty());
    }

    @Test
    void recipe_BuilderPatternSimulation() {
        // Simulate builder pattern usage
        Recipe builtRecipe = new Recipe();
        builtRecipe.setTitle("Built Recipe");
        builtRecipe.setIngredients(Arrays.asList("Salt", "Pepper"));
        builtRecipe.setSteps(Arrays.asList("Mix", "Cook"));
        builtRecipe.setChefName("Builder Chef");
        builtRecipe.setAuthorType(AuthorType.PARTICIPANT);
        builtRecipe.setSeason(Integer.valueOf(3));
        builtRecipe.setSeq(Long.valueOf(999L));

        // Verify all fields are set
        assertEquals("Built Recipe", builtRecipe.getTitle());
        assertEquals(2, builtRecipe.getIngredients().size());
        assertEquals(2, builtRecipe.getSteps().size());
        assertEquals("Builder Chef", builtRecipe.getChefName());
        assertEquals(AuthorType.PARTICIPANT, builtRecipe.getAuthorType());
        assertEquals(3, builtRecipe.getSeason());
        assertEquals(999L, builtRecipe.getSeq());
    }

    @Test
    void recipe_WithMinimalRequiredFields() {
        // Test with only required fields (excluding optional ones)
        Recipe minimalRecipe = new Recipe();
        minimalRecipe.setTitle("Minimal Recipe");
        minimalRecipe.setIngredients(Arrays.asList("One Ingredient"));
        minimalRecipe.setSteps(Arrays.asList("One Step"));
        minimalRecipe.setChefName("Minimal Chef");
        minimalRecipe.setAuthorType(AuthorType.CHEF);

        Set<ConstraintViolation<Recipe>> violations = validator.validate(minimalRecipe);
        assertTrue(violations.isEmpty(), "Minimal recipe should be valid");
    }

    @Test
    void listFields_ShouldHandleNullAndEmptyCorrectly() {
        // Test ingredients list behavior
        recipe.setIngredients(null);
        assertNull(recipe.getIngredients());

        recipe.setIngredients(Arrays.asList());
        assertNotNull(recipe.getIngredients());
        assertTrue(recipe.getIngredients().isEmpty());

        // Test steps list behavior
        recipe.setSteps(null);
        assertNull(recipe.getSteps());

        recipe.setSteps(Arrays.asList());
        assertNotNull(recipe.getSteps());
        assertTrue(recipe.getSteps().isEmpty());
    }

    @Test
    void timestampManagement_ForMongoDB() {
        // Test timestamp behavior typical for MongoDB entities
        Instant now = Instant.now();
        recipe.setCreatedAt(now);
        recipe.setUpdatedAt(now);

        // Simulate update
        Instant later = now.plusSeconds(300);
        recipe.setUpdatedAt(later);

        assertEquals(now, recipe.getCreatedAt());
        assertEquals(later, recipe.getUpdatedAt());
        assertNotEquals(recipe.getCreatedAt(), recipe.getUpdatedAt());
    }

    private Recipe createValidRecipe() {
        Recipe validRecipe = new Recipe();
        validRecipe.setTitle("Valid Recipe");
        validRecipe.setIngredients(Arrays.asList("Ingredient 1", "Ingredient 2"));
        validRecipe.setSteps(Arrays.asList("Step 1", "Step 2"));
        validRecipe.setChefName("Valid Chef");
        validRecipe.setAuthorType(AuthorType.CHEF);
        validRecipe.setSeason(Integer.valueOf(1));
        return validRecipe;
    }
}