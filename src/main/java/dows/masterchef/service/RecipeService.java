package dows.masterchef.service;
import dows.masterchef.dto.RecipeInput;
import dows.masterchef.exception.ApiException;
import dows.masterchef.model.AuthorType;
import dows.masterchef.model.Recipe;
import dows.masterchef.repository.RecipeRepository;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import java.util.List;
@Service
public class RecipeService {
    private final RecipeRepository repo;
    private final SequenceService sequenceService;
    public RecipeService(RecipeRepository repo, SequenceService sequenceService) {
        this.repo = repo; this.sequenceService = sequenceService;
    }
    private void ensureParticipantSeason(RecipeInput in) {
        if (in.getAuthorType() == AuthorType.PARTICIPANT && in.getSeason() == null) {
            throw new ApiException(400, "season is required when authorType is participant");
        }
    }
    public Recipe create(RecipeInput in) {
        ensureParticipantSeason(in);
        Recipe r = new Recipe();
        r.setSeq(Long.valueOf(sequenceService.getNextSequence("recipes")));
        r.setTitle(in.getTitle());
        r.setIngredients(in.getIngredients());
        r.setSteps(in.getSteps());
        r.setChefName(in.getChefName());
        r.setAuthorType(in.getAuthorType());
        r.setSeason(in.getSeason());
        return repo.save(r);
    }
    public List<Recipe> findAll() { return repo.findAllByOrderBySeqAsc(); }
    public Recipe findBySeq(long seq) {
        return repo.findBySeq(Long.valueOf(seq)).orElseThrow(() -> new ApiException(404, "Recipe not found"));
    }
    public List<Recipe> byType(AuthorType type) { return repo.findAllByAuthorTypeOrderBySeqAsc(type); }
    public List<Recipe> bySeason(int season) { return repo.findAllByAuthorTypeAndSeasonOrderBySeqAsc(AuthorType.PARTICIPANT, Integer.valueOf(season)); }
    public List<Recipe> searchByIngredient(String ingredient) {
        if (!StringUtils.hasText(ingredient)) { throw new ApiException(400, "ingredient query param is required"); }
        return repo.searchByIngredientRegex(ingredient);
    }
    public Recipe update(long seq, RecipeInput in) {
        ensureParticipantSeason(in);
        Recipe existing = findBySeq(seq);
        existing.setTitle(in.getTitle());
        existing.setIngredients(in.getIngredients());
        existing.setSteps(in.getSteps());
        existing.setChefName(in.getChefName());
        existing.setAuthorType(in.getAuthorType());
        existing.setSeason(in.getSeason());
        return repo.save(existing);
    }
    public void delete(long seq) {
        Recipe existing = findBySeq(seq);
        repo.delete(existing);
    }
}
