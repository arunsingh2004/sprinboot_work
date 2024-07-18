package millet.demo.repo;

import millet.demo.models.Student;
import java.util.Optional;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface StudentRrepository extends MongoRepository<Student, ObjectId> {
      Optional<Student> findByEmail(String email);
      Optional<Student> findById(String id);
}
