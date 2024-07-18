package millet.demo.repo;

import millet.demo.models.Testimonial;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface TestimonialRepository extends MongoRepository<Testimonial, ObjectId> {
}
