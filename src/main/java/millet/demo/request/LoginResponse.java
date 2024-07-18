package millet.demo.request;

import org.bson.types.ObjectId;

import com.fasterxml.jackson.annotation.JsonProperty;
// import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;

// import lombok.AllArgsConstructor;
// import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
// @AllArgsConstructor
@NoArgsConstructor
// @Builder
@ToString
public class LoginResponse {
    private String token;
           @JsonProperty("id")
    @JsonSerialize(using = ToStringSerializer.class)
    private ObjectId id;
    private String name;
    private String email;
    private String College;
//     private String email;

    // Getter
    public LoginResponse(String token, ObjectId id, String name, String email,String College) {
        this.token = token;
        this.id=id;
        this.name = name;
        this.email = email;
        this.College= College;
    }

    // Getters and setters
    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
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

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
    public String getCollege() {
        return College;
    }

    public void setCollege(String College) {
        this.College = College;
    }

}