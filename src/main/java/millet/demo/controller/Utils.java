// package millet.demo.controller;

// import com.razorpay.Utils;
// import java.util.Map;

// public class Utils {
//     public static boolean verifyPaymentSignature(Map<String, String> paymentData, String secret) {
//         try {
//             // Use Utils.verifyPaymentSignature from Razorpay library
//             String generatedSignature = Utils.getRazorpaySignature(paymentData, secret);
//             return generatedSignature.equals(paymentData.get("razorpay_signature"));
//         } catch (Exception e) {
//             throw new RuntimeException("Error verifying payment signature", e);
//         }
//     }
// }

