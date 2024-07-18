package millet.demo.repo;

import millet.demo.models.Orders;

import java.util.Optional;

import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface OrderRepository extends MongoRepository<Orders, ObjectId> {

    Optional<Orders> findById(ObjectId id);

    Optional<Orders> findByRazorpayOrderId(String razorpayOrderId);
}
