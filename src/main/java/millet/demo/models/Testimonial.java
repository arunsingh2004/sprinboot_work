package millet.demo.models;

import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection="testmonial")
public class Testimonial {
    @Id
    private ObjectId id;
    private String name;
    private String profileUrl;

private String message;
    private String imageUrl;
    private String videoUrl;

public ObjectId getId() {
      return id;
}
public void setId(ObjectId id) {
      this.id = id;
}
public String getName() {
      return name;
}
public void setName(String name) {
      this.name = name;
}
public String getProfilepic() {
      return profileUrl;
}
public void setProfilepic(String profileUrl) {
      this.profileUrl = profileUrl;
}
public String getMessage() {
      return message;
}
public void setMessage(String message) {
      this.message = message;
}
public String getImageUrl() {
      return imageUrl;
}
public void setImageUrl(String imageUrl) {
      this.imageUrl = imageUrl;
}
public String getVideoUrl() {
      return videoUrl;
}
public void setVideoUrl(String videoUrl) {
      this.videoUrl = videoUrl;
}


}
