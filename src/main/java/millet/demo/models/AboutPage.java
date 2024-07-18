package millet.demo.models;

import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

// import java.util.List;

@Document(collection="about_page")
public class AboutPage {
    @Id
    private ObjectId id;
    private String banner;
    private Section journey;
    private Section promises;
    private Section ingredient;
    private String title;
    private String description;

    public AboutPage() {}

    public AboutPage(String banner, Section journey, Section promises, Section ingredient, String title, String description) {
        this.banner = banner;
        this.journey = journey;
        this.promises = promises;
        this.ingredient = ingredient;
        this.title = title;
        this.description = description;
    }

    // Getters and Setters

    public ObjectId getId() {
        return id;
    }

    public void setId(ObjectId id) {
        this.id = id;
    }

    public String getBanner() {
        return banner;
    }

    public void setBanner(String banner) {
        this.banner = banner;
    }

    public Section getJourney() {
        return journey;
    }

    public void setJourney(Section journey) {
        this.journey = journey;
    }

    public Section getPromises() {
        return promises;
    }

    public void setPromises(Section promises) {
        this.promises = promises;
    }

    public Section getIngredient() {
        return ingredient;
    }

    public void setIngredient(Section ingredient) {
        this.ingredient = ingredient;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public static class Section {
        private String title;
        private String description;

        public Section() {}

        public Section(String title, String description) {
            this.title = title;
            this.description = description;
        }

        // Getters and Setters

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getDescription() {
            return description;
        }

        public void setDescription(String description) {
            this.description = description;
        }
    }
}
