package dows.masterchef.controller;
import dows.masterchef.dto.RecipeInput;
import dows.masterchef.model.AuthorType;
import dows.masterchef.model.Recipe;
import dows.masterchef.service.RecipeService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import java.util.List;
@RestController
@RequestMapping("/api/v1/recipes")
public class RecipeController {
    private final RecipeService service;
    public RecipeController(RecipeService service) { this.service = service; }
    @PostMapping @ResponseStatus(HttpStatus.CREATED)
    public Recipe create(@Valid @RequestBody RecipeInput in) { return service.create(in); }
    @PostMapping("/viewer") @ResponseStatus(HttpStatus.CREATED)
    public Recipe createViewer(@Valid @RequestBody RecipeInput in) { in.setAuthorType(AuthorType.VIEWER); return service.create(in); }
    @PostMapping("/participant") @ResponseStatus(HttpStatus.CREATED)
    public Recipe createParticipant(@Valid @RequestBody RecipeInput in) { in.setAuthorType(AuthorType.PARTICIPANT); return service.create(in); }
    @PostMapping("/chef") @ResponseStatus(HttpStatus.CREATED)
    public Recipe createChef(@Valid @RequestBody RecipeInput in) { in.setAuthorType(AuthorType.CHEF); return service.create(in); }
    @GetMapping public List<Recipe> all() { return service.findAll(); }
    @GetMapping("/{seq}") public Recipe bySeq(@PathVariable long seq) { return service.findBySeq(seq); }
    @GetMapping("/type/{type}") public List<Recipe> byType(@PathVariable AuthorType type) { return service.byType(type); }
    @GetMapping("/season/{season}") public List<Recipe> bySeason(@PathVariable int season) { return service.bySeason(season); }
    @GetMapping("/search/by-ingredient") public List<Recipe> search(@RequestParam String ingredient) { return service.searchByIngredient(ingredient); }
    @PutMapping("/{seq}") public Recipe update(@PathVariable long seq, @Valid @RequestBody RecipeInput in) { return service.update(seq, in); }
    @DeleteMapping("/{seq}") @ResponseStatus(HttpStatus.NO_CONTENT) public void delete(@PathVariable long seq) { service.delete(seq); }
}
