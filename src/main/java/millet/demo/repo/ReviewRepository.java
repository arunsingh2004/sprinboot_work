package millet.demo.repo;

import millet.demo.models.Review;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface ReviewRepository extends MongoRepository<Review, ObjectId> {
    List<Review> findByProductId(String productId);
}
