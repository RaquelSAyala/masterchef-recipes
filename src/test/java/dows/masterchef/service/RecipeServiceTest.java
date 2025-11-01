package dows.masterchef.service;

import dows.masterchef.dto.RecipeInput;
import dows.masterchef.exception.ApiException;
import dows.masterchef.model.AuthorType;
import dows.masterchef.model.Recipe;
import dows.masterchef.repository.RecipeRepository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class RecipeServiceTest {

    private RecipeRepository repo;
    private SequenceService sequence;
    private RecipeService service;

    @BeforeEach
    void setup() {
        repo = mock(RecipeRepository.class);
        sequence = mock(SequenceService.class);
        service = new RecipeService(repo, sequence);
    }

    private RecipeInput baseInput(AuthorType type) {
        RecipeInput in = new RecipeInput();
        in.setTitle("Arepas");
        in.setIngredients(List.of("Harina", "Queso"));
        in.setSteps(List.of("Mezclar", "Asar"));
        in.setChefName("Carla");
        in.setAuthorType(type);
        return in;
    }

    @Test
    void create_viewer_ok_sets_seq_and_saves() {
        when(sequence.getNextSequence("recipes")).thenReturn(Long.valueOf(10L));
        ArgumentCaptor<Recipe> captor = ArgumentCaptor.forClass(Recipe.class);
        when(repo.save(any())).thenAnswer(a -> a.getArgument(0));

        RecipeInput in = baseInput(AuthorType.VIEWER);
        Recipe out = service.create(in);

        verify(repo).save(captor.capture());
        Recipe saved = captor.getValue();
        assertEquals(10L, saved.getSeq());
        assertEquals("Arepas", out.getTitle());
    }

    @Test
    void create_participant_without_season_throws_400() {
        RecipeInput in = baseInput(AuthorType.PARTICIPANT);
        in.setSeason(null); // falta temporada
        ApiException ex = assertThrows(ApiException.class, () -> service.create(in));
        assertEquals(400, ex.getStatus());
        assertTrue(ex.getMessage().toLowerCase().contains("season"));
    }

    @Test
    void findAll_returns_sorted_list() {
        Recipe r1 = new Recipe(); r1.setSeq(Long.valueOf(1L));
        Recipe r2 = new Recipe(); r2.setSeq(Long.valueOf(2L));
        when(repo.findAllByOrderBySeqAsc()).thenReturn(Arrays.asList(r1, r2));
        List<Recipe> list = service.findAll();
        assertEquals(2, list.size());
        assertEquals(1L, list.get(0).getSeq());
    }

    @Test
    void findBySeq_found_ok() {
        Recipe r = new Recipe(); r.setSeq(Long.valueOf(7L));
        when(repo.findBySeq(Long.valueOf(7L))).thenReturn(Optional.of(r));
        Recipe out = service.findBySeq(7L);
        assertEquals(7L, out.getSeq());
    }

    @Test
    void findBySeq_not_found_404() {
        when(repo.findBySeq(Long.valueOf(999L))).thenReturn(Optional.empty());
        ApiException ex = assertThrows(ApiException.class, () -> service.findBySeq(999L));
        assertEquals(404, ex.getStatus());
    }

    @Test
    void searchByIngredient_blank_throws_400() {
        ApiException ex = assertThrows(ApiException.class, () -> service.searchByIngredient("   "));
        assertEquals(400, ex.getStatus());
    }

    @Test
    void update_ok_overwrites_fields() {
        Recipe existing = new Recipe();
        existing.setSeq(Long.valueOf(3L));
        when(repo.findBySeq(Long.valueOf(3L))).thenReturn(Optional.of(existing));
        when(repo.save(any())).thenAnswer(a -> a.getArgument(0));

        RecipeInput in = baseInput(AuthorType.CHEF);
        Recipe out = service.update(3L, in);

        assertEquals("Arepas", out.getTitle());
        assertEquals(AuthorType.CHEF, out.getAuthorType());
    }

    @Test
    void delete_ok_removes_entity() {
        Recipe existing = new Recipe(); existing.setSeq(Long.valueOf(5L));
        when(repo.findBySeq(Long.valueOf(5L))).thenReturn(Optional.of(existing));
        service.delete(5L);
        verify(repo).delete(existing);
    }
}
