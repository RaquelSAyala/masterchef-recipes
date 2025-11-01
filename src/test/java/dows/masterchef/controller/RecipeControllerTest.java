package dows.masterchef.controller;

import dows.masterchef.dto.RecipeInput;
import dows.masterchef.model.AuthorType;
import dows.masterchef.model.Recipe;
import dows.masterchef.service.RecipeService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class RecipeControllerTest {

    @Mock
    private RecipeService recipeService;

    @InjectMocks
    private RecipeController recipeController;

    private RecipeInput recipeInput;
    private Recipe recipe;
    private final Long SEQ = (Long) 1L;
    private final int SEASON = 1;

    @BeforeEach
    void setUp() {
        recipeInput = new RecipeInput();
        recipeInput.setTitle("Test Recipe");


        recipe = new Recipe();
        recipe.setSeq(SEQ);
        recipe.setTitle("Test Recipe");

    }

    @Test
    void create_ShouldReturnCreatedRecipe() {
        // Arrange
        when(recipeService.create(any(RecipeInput.class))).thenReturn(recipe);

        // Act
        Recipe result = recipeController.create(recipeInput);

        // Assert
        assertNotNull(result);
        assertEquals(SEQ, result.getSeq());
        assertEquals("Test Recipe", result.getTitle());
        verify(recipeService, times(1)).create(recipeInput);
    }

    @Test
    void createViewer_ShouldSetAuthorTypeAndReturnRecipe() {
        // Arrange
        when(recipeService.create(any(RecipeInput.class))).thenReturn(recipe);

        // Act
        Recipe result = recipeController.createViewer(recipeInput);

        // Assert
        assertNotNull(result);
        assertEquals(AuthorType.VIEWER, recipeInput.getAuthorType());
        verify(recipeService, times(1)).create(recipeInput);
    }

    @Test
    void createParticipant_ShouldSetAuthorTypeAndReturnRecipe() {
        // Arrange
        when(recipeService.create(any(RecipeInput.class))).thenReturn(recipe);

        // Act
        Recipe result = recipeController.createParticipant(recipeInput);

        // Assert
        assertNotNull(result);
        assertEquals(AuthorType.PARTICIPANT, recipeInput.getAuthorType());
        verify(recipeService, times(1)).create(recipeInput);
    }

    @Test
    void createChef_ShouldSetAuthorTypeAndReturnRecipe() {
        // Arrange
        when(recipeService.create(any(RecipeInput.class))).thenReturn(recipe);

        // Act
        Recipe result = recipeController.createChef(recipeInput);

        // Assert
        assertNotNull(result);
        assertEquals(AuthorType.CHEF, recipeInput.getAuthorType());
        verify(recipeService, times(1)).create(recipeInput);
    }

    @Test
    void all_ShouldReturnAllRecipes() {
        // Arrange
        List<Recipe> expectedRecipes = Arrays.asList(recipe, new Recipe());
        when(recipeService.findAll()).thenReturn(expectedRecipes);

        // Act
        List<Recipe> result = recipeController.all();

        // Assert
        assertNotNull(result);
        assertEquals(2, result.size());
        verify(recipeService, times(1)).findAll();
    }

    @Test
    void bySeq_WithExistingRecipe_ShouldReturnRecipe() {
        // Arrange
        when(recipeService.findBySeq(SEQ)).thenReturn(recipe);

        // Act
        Recipe result = recipeController.bySeq(SEQ);

        // Assert
        assertNotNull(result);
        assertEquals(SEQ, result.getSeq());
        verify(recipeService, times(1)).findBySeq(SEQ);
    }

    @Test
    void bySeq_WithNonExistingRecipe_ShouldThrowException() {
        // Arrange
        when(recipeService.findBySeq(SEQ)).thenReturn(null);

        // Act & Assert
        Recipe result = recipeController.bySeq(SEQ);
        assertNull(result);
        verify(recipeService, times(1)).findBySeq(SEQ);
    }

    @Test
    void byType_ShouldReturnRecipesByType() {
        // Arrange
        AuthorType type = AuthorType.CHEF;
        List<Recipe> expectedRecipes = Arrays.asList(recipe);
        when(recipeService.byType(type)).thenReturn(expectedRecipes);

        // Act
        List<Recipe> result = recipeController.byType(type);

        // Assert
        assertNotNull(result);
        assertEquals(1, result.size());
        verify(recipeService, times(1)).byType(type);
    }

    @Test
    void bySeason_ShouldReturnRecipesBySeason() {
        // Arrange
        List<Recipe> expectedRecipes = Arrays.asList(recipe);
        when(recipeService.bySeason(SEASON)).thenReturn(expectedRecipes);

        // Act
        List<Recipe> result = recipeController.bySeason(SEASON);

        // Assert
        assertNotNull(result);
        assertEquals(1, result.size());
        verify(recipeService, times(1)).bySeason(SEASON);
    }

    @Test
    void searchByIngredient_ShouldReturnMatchingRecipes() {
        // Arrange
        String ingredient = "tomato";
        List<Recipe> expectedRecipes = Arrays.asList(recipe);
        when(recipeService.searchByIngredient(ingredient)).thenReturn(expectedRecipes);

        // Act
        List<Recipe> result = recipeController.search(ingredient);

        // Assert
        assertNotNull(result);
        assertEquals(1, result.size());
        verify(recipeService, times(1)).searchByIngredient(ingredient);
    }

    @Test
    void update_WithExistingRecipe_ShouldReturnUpdatedRecipe() {
        // Arrange
        Recipe updatedRecipe = new Recipe();
        updatedRecipe.setSeq(SEQ);
        updatedRecipe.setTitle("Updated Recipe");

        when(recipeService.update(eq(SEQ), any(RecipeInput.class))).thenReturn(updatedRecipe);

        // Act
        Recipe result = recipeController.update(SEQ, recipeInput);

        // Assert
        assertNotNull(result);
        assertEquals("Updated Recipe", result.getTitle());
        verify(recipeService, times(1)).update(SEQ, recipeInput);
    }

    @Test
    void update_WithNonExistingRecipe_ShouldThrowException() {
        // Arrange
        when(recipeService.update(eq(SEQ), any(RecipeInput.class))).thenReturn(null);

        // Act & Assert
        Recipe result = recipeController.update(SEQ, recipeInput);
        assertNull(result);
        verify(recipeService, times(1)).update(SEQ, recipeInput);
    }

    @Test
    void delete_ShouldCallServiceDelete() {
        // Act
        recipeController.delete(SEQ);

        // Assert
        verify(recipeService, times(1)).delete(SEQ);
    }

    @Test
    void delete_WithNonExistingRecipe_ShouldNotThrowException() {
        // Arrange
        doNothing().when(recipeService).delete(SEQ);

        // Act & Assert
        assertDoesNotThrow(() -> recipeController.delete(SEQ));
        verify(recipeService, times(1)).delete(SEQ);
    }

    // Test para verificar el comportamiento con parámetros nulos o inválidos
    @Test
    void create_WithNullInput_ShouldThrowException() {
        // Arrange
        when(recipeService.create(null)).thenThrow(new IllegalArgumentException());

        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> recipeController.create(null));
    }

    @Test
    void search_WithEmptyIngredient_ShouldReturnEmptyList() {
        // Arrange
        when(recipeService.searchByIngredient("")).thenReturn(Arrays.asList());

        // Act
        List<Recipe> result = recipeController.search("");

        // Assert
        assertNotNull(result);
        assertTrue(result.isEmpty());
        verify(recipeService, times(1)).searchByIngredient("");
    }

    @Test
    void bySeason_WithInvalidSeason_ShouldReturnEmptyList() {
        // Arrange
        int invalidSeason = -1;
        when(recipeService.bySeason(invalidSeason)).thenReturn(Arrays.asList());

        // Act
        List<Recipe> result = recipeController.bySeason(invalidSeason);

        // Assert
        assertNotNull(result);
        assertTrue(result.isEmpty());
        verify(recipeService, times(1)).bySeason(invalidSeason);
    }
}