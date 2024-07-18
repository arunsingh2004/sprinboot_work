package millet.demo.service;

import com.razorpay.Order;
import com.razorpay.RazorpayClient;
import com.razorpay.RazorpayException;
import com.razorpay.Utils;
import millet.demo.models.Orders;
import millet.demo.repo.OrderRepository;
import org.json.JSONObject;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public class PaymentService {

    private String keyId = "rzp_test_Ub6RA6s8MhTAUP";
    private String keySecret = "DumcIMQVm8FfYJci5LdtTuG6";

    private final OrderRepository orderRepository;
    private RazorpayClient client;

    public PaymentService(OrderRepository orderRepository) throws RazorpayException {
        this.orderRepository = orderRepository;
        this.client = new RazorpayClient(keyId, keySecret);
    }

    public JSONObject createOrder(String customerId, List<Map<String, Object>> products, double totalAmount) throws RazorpayException {
        JSONObject orderRequest = new JSONObject();
        orderRequest.put("amount", totalAmount * 100); // amount in the smallest currency unit
        orderRequest.put("currency", "INR");
        orderRequest.put("receipt", "txn_123456");

        Order razorpayOrder = client.orders.create(orderRequest);

        // Save the order details to the database
        Orders orderDetails = new Orders();
        orderDetails.setCustomerId(customerId);
        orderDetails.setProducts(products);
        orderDetails.setTotalAmount(totalAmount);
        orderDetails.setRazorpayOrderId(razorpayOrder.get("id"));
        orderRepository.save(orderDetails);

        return razorpayOrder.toJson();
    }

    public void verifyPayment(String razorpayOrderId, String razorpayPaymentId, String razorpaySignature) throws RazorpayException {
        Optional<Orders> optionalOrder = orderRepository.findByRazorpayOrderId(razorpayOrderId);
        if (optionalOrder.isPresent()) {
            Orders order = optionalOrder.get();

            JSONObject options = new JSONObject();
            options.put("razorpay_order_id", razorpayOrderId);
            options.put("razorpay_payment_id", razorpayPaymentId);
            options.put("razorpay_signature", razorpaySignature);

            boolean isValid = Utils.verifyPaymentSignature(options, keySecret);

            if (isValid) {
                order.setStatus("paid");
                orderRepository.save(order);
            } else {
                throw new RuntimeException("Payment verification failed");
            }
        } else {
            throw new RuntimeException("Order not found for razorpayOrderId: " + razorpayOrderId);
        }
    }
}
