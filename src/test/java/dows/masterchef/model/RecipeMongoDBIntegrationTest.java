package dows.masterchef.model;

import jakarta.validation.ConstraintViolation;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.time.Instant;
import java.util.Arrays;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class RecipeMongoDBIntegrationTest {

    @Test
    void class_ShouldBePublic() {
        assertTrue(Modifier.isPublic(Recipe.class.getModifiers()));
    }

    @Test
    void fields_ShouldHavePrivateModifiers() throws NoSuchFieldException {
        Field idField = Recipe.class.getDeclaredField("id");
        Field titleField = Recipe.class.getDeclaredField("title");

        assertTrue(Modifier.isPrivate((idField).getModifiers()));
        assertTrue(Modifier.isPrivate((titleField).getModifiers()));
    }

    @Test
    void methods_ShouldBePublic() throws NoSuchMethodException {
        assertTrue(Modifier.isPublic(Recipe.class.getMethod("getId").getModifiers()));
        assertTrue(Modifier.isPublic(Recipe.class.getMethod("setId", String.class).getModifiers()));
        assertTrue(Modifier.isPublic(Recipe.class.getMethod("getTitle").getModifiers()));
        assertTrue(Modifier.isPublic(Recipe.class.getMethod("setTitle", String.class).getModifiers()));
    }

    @Test
    void recipe_SimulateMongoDBSaveOperation() {
        // Simulate a recipe before MongoDB save (no ID)
        Recipe newRecipe = new Recipe();
        newRecipe.setTitle("New Recipe");
        newRecipe.setIngredients(Arrays.asList("Flour", "Sugar"));
        newRecipe.setSteps(Arrays.asList("Mix", "Bake"));
        newRecipe.setChefName("New Chef");
        newRecipe.setAuthorType(AuthorType.VIEWER);
        newRecipe.setSeason(Integer.valueOf(1));

        // Simulate MongoDB assigning ID and timestamps
        newRecipe.setId("507f1f77bcf86cd799439012");
        newRecipe.setSeq(Long.valueOf(100L));
        Instant now = Instant.now();
        newRecipe.setCreatedAt(now);
        newRecipe.setUpdatedAt(now);

        // Verify the saved recipe
        assertNotNull(newRecipe.getId());
        assertNotNull(newRecipe.getSeq());
        assertNotNull(newRecipe.getCreatedAt());
        assertNotNull(newRecipe.getUpdatedAt());
    }

    @Test
    void recipe_SimulateMongoDBUpdateOperation() {
        // Simulate an existing recipe
        Recipe existingRecipe = new Recipe();
        existingRecipe.setId("507f1f77bcf86cd799439013");
        existingRecipe.setSeq(Long.valueOf(101L));
        existingRecipe.setTitle("Original Title");
        existingRecipe.setIngredients(Arrays.asList("Original Ingredient"));
        existingRecipe.setSteps(Arrays.asList("Original Step"));
        existingRecipe.setChefName("Original Chef");
        existingRecipe.setAuthorType(AuthorType.CHEF);
        existingRecipe.setSeason(Integer.valueOf(2));
        Instant originalTime = Instant.now().minusSeconds(3600);
        existingRecipe.setCreatedAt(originalTime);
        existingRecipe.setUpdatedAt(originalTime);

        // Simulate update
        existingRecipe.setTitle("Updated Title");
        existingRecipe.setSeason(Integer.valueOf(3));
        Instant updateTime = Instant.now();
        existingRecipe.setUpdatedAt(updateTime);

        // Verify update behavior
        assertEquals("Updated Title", existingRecipe.getTitle());
        assertEquals(3, existingRecipe.getSeason());
        assertEquals(originalTime, existingRecipe.getCreatedAt()); // Should not change
        assertEquals(updateTime, existingRecipe.getUpdatedAt()); // Should be updated
    }


}