package dows.masterchef.model;

import java.time.Instant;
import java.util.List;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

@Document(collection = "recipes")
public class Recipe {
    @Id private String id;
    @Indexed(unique = true) private Long seq;
    @NotBlank private String title;
    @NotEmpty private List<@NotBlank String> ingredients;
    @NotEmpty private List<@NotBlank String> steps;
    @NotBlank private String chefName;
    @NotNull private AuthorType authorType;
    private Integer season;
    @CreatedDate private Instant createdAt;
    @LastModifiedDate private Instant updatedAt;
    // getters/setters
    public String getId() { return id; } public void setId(String id) { this.id = id; }
    public Long getSeq() { return seq; } public void setSeq(Long seq) { this.seq = seq; }
    public String getTitle() { return title; } public void setTitle(String title) { this.title = title; }
    public List<String> getIngredients() { return ingredients; } public void setIngredients(List<String> ingredients) { this.ingredients = ingredients; }
    public List<String> getSteps() { return steps; } public void setSteps(List<String> steps) { this.steps = steps; }
    public String getChefName() { return chefName; } public void setChefName(String chefName) { this.chefName = chefName; }
    public AuthorType getAuthorType() { return authorType; } public void setAuthorType(AuthorType authorType) { this.authorType = authorType; }
    public Integer getSeason() { return season; } public void setSeason(Integer season) { this.season = season; }
    public Instant getCreatedAt() { return createdAt; } public void setCreatedAt(Instant createdAt) { this.createdAt = createdAt; }
    public Instant getUpdatedAt() { return updatedAt; } public void setUpdatedAt(Instant updatedAt) { this.updatedAt = updatedAt; }
}
