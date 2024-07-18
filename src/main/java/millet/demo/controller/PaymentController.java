// package millet.demo.controller;

// import millet.demo.service.PaymentService;
// import org.json.JSONObject;
// import org.slf4j.Logger;
// import org.slf4j.LoggerFactory;
// import org.springframework.beans.factory.annotation.Autowired;
// import org.springframework.http.MediaType;
// import org.springframework.http.ResponseEntity;
// import org.springframework.web.bind.annotation.*;

// import java.util.List;
// import java.util.Map;

// @RestController
// @RequestMapping("/api/payment")
// public class PaymentController {

//     private static final Logger logger = LoggerFactory.getLogger(PaymentController.class);

//     @Autowired
//     private PaymentService paymentService;

//     @PostMapping(value = "/create-order", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
//     public ResponseEntity<?> createOrder(@RequestBody Map<String, Object> orderData) {
//         try {
//             String customerId = (String) orderData.get("customerId");
//             @SuppressWarnings("unchecked")
//             List<Map<String, Object>> products = (List<Map<String, Object>>) orderData.get("products");
//             double totalAmount = ((Number) orderData.get("totalAmount")).doubleValue();

//             JSONObject order = paymentService.createOrder(customerId, products, totalAmount);
//             return ResponseEntity.ok(order.toString());
//         } catch (Exception e) {
//             logger.error("Error creating order", e);
//             return ResponseEntity.status(500).body("Error creating order: " + e.getMessage());
//         }
//     }

//     @PostMapping(value = "/verify-payment", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
//     public ResponseEntity<?> verifyPayment(@RequestBody Map<String, String> paymentData) {
//         try {
//             String razorpayOrderId = paymentData.get("razorpay_order_id");
//             String razorpayPaymentId = paymentData.get("razorpay_payment_id");
//             String razorpaySignature = paymentData.get("razorpay_signature");

//             paymentService.verifyPayment(razorpayOrderId, razorpayPaymentId, razorpaySignature);
//             return ResponseEntity.ok("Payment verified successfully");
//         } catch (Exception e) {
//             logger.error("Error verifying payment", e);
//             return ResponseEntity.status(500).body("Error verifying payment: " + e.getMessage());
//         }
//     }
// }
package millet.demo.controller;

import com.razorpay.Order;
import com.razorpay.RazorpayClient;
import com.razorpay.Utils;
import millet.demo.models.Orders;
import millet.demo.repo.OrderRepository;
import millet.demo.request.VerifyPaymentRequest;

import org.apache.http.HttpStatus;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/api/payment")
public class PaymentController {

    @Autowired
    private OrderRepository orderRepository;

    @PostMapping(value = "/create-order", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> createOrder(@RequestBody Orders orderRequest) {
        try {
            RazorpayClient razorpay = new RazorpayClient("rzp_test_Ub6RA6s8MhTAUP", "DumcIMQVm8FfYJci5LdtTuG6");

            JSONObject orderRequestJson = new JSONObject();
            orderRequestJson.put("amount", orderRequest.getTotalAmount() * 100); // amount in the smallest currency unit
            orderRequestJson.put("currency", "INR");
            orderRequestJson.put("receipt", "order_rcptid_11");

            Order order = razorpay.orders.create(orderRequestJson);

            orderRequest.setRazorpayOrderId(order.get("id"));
            orderRequest.setStatus("UNPAID");
            orderRepository.save(orderRequest);

            return ResponseEntity.ok(order.toString()); // Ensure the response is in JSON format
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Error creating order: " + e.getMessage());
        }
    }

   
    @PostMapping(value = "/verify-payment", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> verifyPayment(@RequestBody VerifyPaymentRequest verifyPaymentRequest) {
        try {
            String razorpayOrderId = verifyPaymentRequest.getRazorpayOrderId();
            @SuppressWarnings("unused")
            String razorpayPaymentId = verifyPaymentRequest.getRazorpayPaymentId();
            @SuppressWarnings("unused")
            String razorpaySignature = verifyPaymentRequest.getRazorpaySignature();
           // Convert the map to JSONObject
           JSONObject json = new JSONObject(verifyPaymentRequest.toMap());

           // Use Razorpay's Utils class for signature verification
           boolean isValid = Utils.verifyPaymentSignature(json, "DumcIMQVm8FfYJci5LdtTuG6");
           System.out.println("razorpayOrderId: " + verifyPaymentRequest.getRazorpayOrderId());
System.out.println("razorpayPaymentId: " + verifyPaymentRequest.getRazorpayPaymentId());
System.out.println("razorpaySignature: " + verifyPaymentRequest.getRazorpaySignature());

           System.out.println("Verify Payment Request: " + json.toString());
            if (isValid) {
                Optional<Orders> optionalOrder = orderRepository.findByRazorpayOrderId(razorpayOrderId);
                if (optionalOrder.isPresent()) {
                    Orders order = optionalOrder.get();
                    order.setStatus("PAID");
                    orderRepository.save(order);
                    return ResponseEntity.ok("Payment verified successfully");
                } else {
                    return ResponseEntity.status(HttpStatus.SC_NOT_FOUND).body("Order not found for razorpayOrderId: " + razorpayOrderId);
                }
            } else {
                return ResponseEntity.status(HttpStatus.SC_BAD_REQUEST).body("Payment verification failed");
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.SC_INTERNAL_SERVER_ERROR).body("Error verifying payment: " + e.getMessage());
        }
    }

}
