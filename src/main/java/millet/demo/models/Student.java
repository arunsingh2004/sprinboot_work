package millet.demo.models;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import org.bson.types.ObjectId;
import com.mongodb.lang.NonNull;

@Document(collection="students")
public class Student {
    @Id
    private ObjectId id;
    private String name;
    private String city;
    private String college;
    private String phonenumber;
    private String profilePicture;

  
    @Indexed(unique = true)@NonNull
    private String email;
    @NonNull
    private String password;
  

    public Student() {}

    public Student( String name, String city, String college, String email, String password,String phonenumber, String profilePicture) {
        this.name = name;
        this.city = city;
        this.college = college;
        this.phonenumber= phonenumber;
        this.profilePicture = profilePicture;
        this.email= email;
        this.password= password;

    }
    public String getProfilePicture() {
        return profilePicture;
    }

    public void setProfilePicture(String profilePicture) {
        this.profilePicture = profilePicture;
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
    public void setPhone(String phonenumber) {
        this.phonenumber = phonenumber;
    }
    public String getPhone() {
        return phonenumber;
    }


    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getCollege() {
        return college;
    }

    public void setCollege(String college) {
        this.college = college;
    }

    // public String getProfilePicture() {
    //     return profilePicture;
    // }

    // public void setProfilePicture(String profilePicture) {
    //     this.profilePicture = profilePicture;
    // }
    public String getEmail() {
      return email;
}

public void setEmail(String email) {
      this.email = email;
}

public String getPassword() {
      return password;
}

public void setPassword(String password) {
      this.password = password;
}


  
}
