package millet.demo.controller;

import org.bson.types.ObjectId;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import millet.demo.models.Coupon;
import millet.demo.models.Orders;
import millet.demo.repo.CouponRepository;
import millet.demo.repo.OrderRepository;

import java.util.Date;
import java.util.Optional;

@RestController
@RequestMapping("/order")
public class OrderController {

    private static final Logger logger = LoggerFactory.getLogger(OrderController.class);

    @Autowired
    private OrderRepository orderRepository;
    
        @Autowired
    private CouponRepository couponRepository;

     @PostMapping("/")
    public ResponseEntity<?> addOrder(@RequestBody Orders order, @RequestParam(required = false) String couponCode) {
        try {
            double discount = 0.0;

            if (couponCode != null) {
                Optional<Coupon> couponOptional = couponRepository.findByCode(couponCode);
                if (couponOptional.isPresent()) {
                    Coupon coupon = couponOptional.get();
                    if (coupon.getExpirationDate().after(new Date())) {
                        if (coupon.isPercentage()) {
                            discount = order.getTotalAmount() * coupon.getDiscount() / 100;
                        } else {
                            discount = coupon.getDiscount();
                        }
                    } else {
                        return ResponseEntity.status(400).body("Coupon is expired");
                    }
                } else {
                    return ResponseEntity.status(404).body("Coupon not found");
                }
            }

            order.setTotalAmount(order.getTotalAmount() - discount);
            logger.info("Adding order: {}", order);
            Orders savedOrder = orderRepository.save(order);
            return ResponseEntity.ok(savedOrder);
        } catch (Exception e) {
            logger.error("Error adding order", e);
            return ResponseEntity.status(500).body("Error adding order: " + e.getMessage());
        }
    }

    @GetMapping("/")
    public ResponseEntity<?> getOrders() {
        try {
            return ResponseEntity.ok(orderRepository.findAll());
        } catch (Exception e) {
            logger.error("Error fetching orders", e);
            return ResponseEntity.status(500).body("Error fetching orders: " + e.getMessage());
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getOrderById(@PathVariable String id) {
        try {
            ObjectId objectId = new ObjectId(id);
            Optional<Orders> order = orderRepository.findById(objectId);
            if (order.isPresent()) {
                return ResponseEntity.ok(order.get());
            } else {
                return ResponseEntity.status(404).body("Order not found");
            }
        } catch (IllegalArgumentException e) {
            logger.error("Invalid ID format", e);
            return ResponseEntity.status(400).body("Invalid ID format: " + id);
        } catch (Exception e) {
            logger.error("Error fetching order", e);
            return ResponseEntity.status(500).body("Error fetching order: " + e.getMessage());
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateOrder(@PathVariable String id, @RequestBody Orders order) {
        try {
            ObjectId objectId = new ObjectId(id);
            Optional<Orders> optionalOrder = orderRepository.findById(objectId);
            if (optionalOrder.isPresent()) {
                Orders existingOrder = optionalOrder.get();
                existingOrder.setCustomerId(order.getCustomerId());
                existingOrder.setProducts(order.getProducts());
                existingOrder.setTotalAmount(order.getTotalAmount());
                existingOrder.setStatus(order.getStatus());
                existingOrder.setAddress(order.getAddress());
                existingOrder.setPhoneNumber(order.getPhoneNumber());
                existingOrder.setRazorpayOrderId(order.getRazorpayOrderId());
                orderRepository.save(existingOrder);
                return ResponseEntity.ok(existingOrder);
            } else {
                return ResponseEntity.status(404).body("Order not found");
            }
        } catch (IllegalArgumentException e) {
            logger.error("Invalid ID format", e);
            return ResponseEntity.status(400).body("Invalid ID format: " + id);
        } catch (Exception e) {
            logger.error("Error updating order", e);
            return ResponseEntity.status(500).body("Error updating order: " + e.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteOrder(@PathVariable String id) {
        try {
            ObjectId objectId = new ObjectId(id);
            orderRepository.deleteById(objectId);
            return ResponseEntity.ok("Order deleted with id: " + id);
        } catch (IllegalArgumentException e) {
            logger.error("Invalid ID format", e);
            return ResponseEntity.status(400).body("Invalid ID format: " + id);
        } catch (Exception e) {
            logger.error("Error deleting order", e);
            return ResponseEntity.status(500).body("Error deleting order: " + e.getMessage());
        }
    }
}
