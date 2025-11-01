package dows.masterchef.repository;
import java.util.List;
import java.util.Optional;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import dows.masterchef.model.AuthorType;
import dows.masterchef.model.Recipe;
public interface RecipeRepository extends MongoRepository<Recipe, String> {
    Optional<Recipe> findBySeq(Long seq);
    List<Recipe> findAllByAuthorTypeOrderBySeqAsc(AuthorType type);
    List<Recipe> findAllByAuthorTypeAndSeasonOrderBySeqAsc(AuthorType type, Integer season);
    @Query("{ ingredients: { $regex: ?0, $options: 'i' } }")
    List<Recipe> searchByIngredientRegex(String ingredient);
    List<Recipe> findAllByOrderBySeqAsc();
}
