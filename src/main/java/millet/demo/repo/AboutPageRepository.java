package millet.demo.repo;

import millet.demo.models.AboutPage;
// import millet.demo.models.Orders;

import java.util.Optional;

import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface AboutPageRepository extends MongoRepository<AboutPage, ObjectId> {
       Optional<AboutPage> findById(ObjectId id);
}
