package millet.demo.models;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
// import java.util.List;
// import java.util.Optional;

@Document(collection="bannerAds")
public class BannerAd {
    @Id
    private String id;
    private String type;
    private String title;
    private String description;
    private String imageurl;
public BannerAd() {}

public BannerAd(String id, String type, String title,String description ,String imageurl) {
    this.id = id;
    this.type=type;
    this.title = title;
    this.description = description;
    this.imageurl = imageurl;
}
public String getId() {
    return id;
}
public void setId(String id) {
    this.id = id;
}
public String gettitle() {
    return title;
}
public void settitle(String title){
    this.title=title;
}
public String gettype() {
    return type;
}
public void settype(String type) {
    this.type = type;
}
public String getdescription() {
    return description;
}
public void setdescription(String description) {
    this.description = description;
}
public String getimageurl() {
    return imageurl;
}
public void setimageurl(String imageurl) {
    this.imageurl =imageurl;
}

}