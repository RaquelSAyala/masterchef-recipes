package dows.masterchef.dto;
import java.util.List;
import dows.masterchef.model.AuthorType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
public class RecipeInput {
    @NotBlank private String title;
    @NotEmpty private List<@NotBlank String> ingredients;
    @NotEmpty private List<@NotBlank String> steps;
    @NotBlank private String chefName;
    @NotNull private AuthorType authorType;
    private Integer season;
    public String getTitle() { return title; } public void setTitle(String title) { this.title = title; }
    public List<String> getIngredients() { return ingredients; } public void setIngredients(List<String> ingredients) { this.ingredients = ingredients; }
    public List<String> getSteps() { return steps; } public void setSteps(List<String> steps) { this.steps = steps; }
    public String getChefName() { return chefName; } public void setChefName(String chefName) { this.chefName = chefName; }
    public AuthorType getAuthorType() { return authorType; } public void setAuthorType(AuthorType authorType) { this.authorType = authorType; }
    public Integer getSeason() { return season; } public void setSeason(Integer season) { this.season = season; }
}
