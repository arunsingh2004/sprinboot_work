package millet.demo.controller;

import millet.demo.models.Coupon;
import millet.demo.repo.CouponRepository;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/coupons")
public class CouponController {

    @Autowired
    private CouponRepository couponRepository;

    @PostMapping("/")
    public ResponseEntity<?> addCoupon(@RequestBody Coupon coupon) {
        try {
            Coupon savedCoupon = couponRepository.save(coupon);
            return ResponseEntity.ok(savedCoupon);
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Error adding coupon: " + e.getMessage());
        }
    }

    @GetMapping("/{code}")
    public ResponseEntity<?> getCouponByCode(@PathVariable String code) {
        try {
            Optional<Coupon> coupon = couponRepository.findByCode(code);
            if (coupon.isPresent()) {
                return ResponseEntity.ok(coupon.get());
            } else {
                return ResponseEntity.status(404).body("Coupon not found");
            }
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Error fetching coupon: " + e.getMessage());
        }
    }
}
