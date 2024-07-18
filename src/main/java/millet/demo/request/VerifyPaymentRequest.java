package millet.demo.request;

import java.util.HashMap;
import java.util.Map;

// import org.json.JSONObject;

public class VerifyPaymentRequest {
    private String razorpayOrderId;
    private String razorpayPaymentId;
    private String razorpaySignature;

    // Getters and setters

     public Map<String, String> toMap() {
        Map<String, String> map = new HashMap<>();
        map.put("razorpay_order_id", this.razorpayOrderId);
        map.put("razorpay_payment_id", this.razorpayPaymentId);
        map.put("razorpay_signature", this.razorpaySignature);
        return map;
    }

public String getRazorpayOrderId() {
      return razorpayOrderId;
}

public void setRazorpayOrderId(String razorpayOrderId) {
      this.razorpayOrderId = razorpayOrderId;
}

public String getRazorpayPaymentId() {
      return razorpayPaymentId;
}

public void setRazorpayPaymentId(String razorpayPaymentId) {
      this.razorpayPaymentId = razorpayPaymentId;
}

public String getRazorpaySignature() {
      return razorpaySignature;
}

public void setRazorpaySignature(String razorpaySignature) {
      this.razorpaySignature = razorpaySignature;
}
}
