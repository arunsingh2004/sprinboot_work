package millet.demo.controller;

import millet.demo.models.Student;
import millet.demo.repo.StudentRrepository;
import org.bson.types.ObjectId;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.crypto.password.PasswordEncoder;
import java.util.Optional;

@RestController
@RequestMapping("/student")
public class MyController {

    private static final Logger logger = LoggerFactory.getLogger(MyController.class);

    @Autowired
    private StudentRrepository studentRrepository;

    @PostMapping("/signup")
    public ResponseEntity<?> addStudent(@RequestBody Student student) {
        try {
            logger.info("Adding student: {}", student);
            student.setPassword(passwordEncoder.encode(student.getPassword()));
            Student savedStudent = this.studentRrepository.save(student);
            return ResponseEntity.ok(savedStudent);
        } catch (Exception e) {
            logger.error("Error adding student", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error adding student: " + e.getMessage());
        }
    }

    @GetMapping("/")
    public ResponseEntity<?> getStudents() {
        try {
            return ResponseEntity.ok(this.studentRrepository.findAll());
        } catch (Exception e) {
            logger.error("Error fetching students", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error fetching students: " + e.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteStudent(@PathVariable String id) {
        try {
            ObjectId objectId = new ObjectId(id);
            logger.info("Deleting student with id: {}", objectId);
            Optional<Student> optionalStudent = studentRrepository.findById(objectId);
            if (optionalStudent.isPresent()) {
                this.studentRrepository.deleteById(objectId);
                return ResponseEntity.ok("Student deleted with id: " + id);
            } else {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Student not found with id: " + id);
            }
        } catch (IllegalArgumentException e) {
            logger.error("Invalid ID format", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Invalid ID format: " + id);
        } catch (Exception e) {
            logger.error("Error deleting student", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error deleting student: " + e.getMessage());
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateStudent(@PathVariable String id, @RequestBody Student student) {
        try {
            ObjectId objectId = new ObjectId(id);
            logger.info("Updating student with id: {}", objectId);
            Optional<Student> optionalStudent = studentRrepository.findById(objectId);
            if (optionalStudent.isPresent()) {
                Student existingStudent = optionalStudent.get();
                existingStudent.setName(student.getName());
                existingStudent.setCity(student.getCity());
                existingStudent.setCollege(student.getCollege());
                existingStudent.setEmail(student.getEmail());
                existingStudent.setPhone(student.getPhone());
                existingStudent.setProfilePicture(student.getProfilePicture());
                existingStudent.setPassword(passwordEncoder.encode(student.getPassword()));
                // existingStudent.setProfilePicture(student.getProfilePicture());
                studentRrepository.save(existingStudent);
                return ResponseEntity.ok(existingStudent);
            } else {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Student not found with id: " + id);
            }
        } catch (IllegalArgumentException e) {
            logger.error("Invalid ID format", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Invalid ID format: " + id);
        } catch (Exception e) {
            logger.error("Error updating student", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error updating student: " + e.getMessage());
        }
    }
    

    @Autowired
    private PasswordEncoder passwordEncoder;
}
