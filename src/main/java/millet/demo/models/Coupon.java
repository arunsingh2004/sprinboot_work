package millet.demo.models;


import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;

@Document(collection = "coupons")
public class Coupon {

    @Id
    private ObjectId id;
    private String code;
    private double discount; // Can be percentage or fixed amount
    private Date expirationDate;
    private boolean isPercentage;

    // Constructors, getters, and setters

    public String getCode() {
      return code;
}

public void setCode(String code) {
      this.code = code;
}

public double getDiscount() {
      return discount;
}

public void setDiscount(double discount) {
      this.discount = discount;
}

public Date getExpirationDate() {
      return expirationDate;
}

public void setExpirationDate(Date expirationDate) {
      this.expirationDate = expirationDate;
}

public boolean isPercentage() {
      return isPercentage;
}

public void setPercentage(boolean isPercentage) {
      this.isPercentage = isPercentage;
}

public Coupon() {}

    public Coupon(ObjectId id, String code, double discount, Date expirationDate, boolean isPercentage) {
        this.id = id;
        this.code = code;
        this.discount = discount;
        this.expirationDate = expirationDate;
        this.isPercentage = isPercentage;
    }

 
}
