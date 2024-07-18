package millet.demo.models;

import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection="reviews")
public class Review {
    @Id
    private ObjectId id;
    private String productId;
    private String userId;
    private String comment;
    private int rating;

    public Review() {}

    public Review(String productId, String userId, String comment, int rating){
        this.productId = productId;
        this.userId = userId;
        this.comment = comment;
        this.rating = rating;
    }

    // Getters and Setters

    public ObjectId getId(){
        return id;
    }

    public void setId(ObjectId id) {
        this.id = id;
    }

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public int getRating() {
        return rating;
    }

    public void setRating(int rating) {
        this.rating = rating;
    }

}
