// package millet.demo.controller;

// import millet.demo.models.Student;
// import millet.demo.repo.StudentRrepository;
// import millet.demo.request.LoginRequest;
// import millet.demo.request.LoginResponse;
// import millet.demo.service.GoogleAuthService;
// import millet.demo.util.JwtUtil;
// import org.slf4j.Logger;
// import org.slf4j.LoggerFactory;
// import org.springframework.beans.factory.annotation.Autowired;
// import org.springframework.http.HttpStatus;
// import org.springframework.http.ResponseEntity;
// import org.springframework.security.crypto.password.PasswordEncoder;
// // import org.springframework.security.oauth2.jwt.JwtDecoder;
// // import org.springframework.security.oauth2.jwt.JwtException;
// import org.springframework.web.bind.annotation.*;
// import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;

// // import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;

// import java.util.Map;
// // import jakarta.servlet.http.HttpServletRequest;
// import java.util.Optional;

// @RestController
// @RequestMapping("/auth")
// public class AuthController {

//     private static final Logger logger = LoggerFactory.getLogger(AuthController.class);

//     @Autowired
//     private StudentRrepository studentRrepository;

//     @Autowired
//     private PasswordEncoder passwordEncoder;

//     @Autowired
//     private JwtUtil jwtUtil;
 
//     @Autowired
//     private GoogleAuthService googleAuthService;

//     // @Autowired
//     // private GoogleAuthService googleAuthService;
 
//     // @Autowired
//     // private JwtDecoder jwtDecoder;

//     @PostMapping("/login")
//     public ResponseEntity<?> login(@RequestBody LoginRequest loginRequest) {
//         try {
//             Optional<Student> optionalStudent = studentRrepository.findByEmail(loginRequest.getEmail());
//             if (optionalStudent.isPresent()) {
//                 Student student = optionalStudent.get();
//                 if (passwordEncoder.matches(loginRequest.getPassword(), student.getPassword())) {
//                     String token = jwtUtil.generateToken(student.getEmail());
//                     LoginResponse loginResponse = new LoginResponse(token, student.getId(), student.getName(), student.getEmail(), student.getCollege());
//                     return ResponseEntity.ok(loginResponse);
//                 } else {
//                     return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid email or password");
//                 }
//             } else {
//                 return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid email or password");
//             }
//         } catch (Exception e) {
//             logger.error("Error logging in", e);
//             return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error logging in");
//         }
//     }

//     @PostMapping("/signup")
//     public ResponseEntity<?> addStudent(@RequestBody Student student) {
//         try {
//             logger.info("Adding student: {}", student);
//             student.setPassword(passwordEncoder.encode(student.getPassword()));
//             Student savedStudent = studentRrepository.save(student);
//             return ResponseEntity.ok(savedStudent);
//         } catch (Exception e) {
//             logger.error("Error adding student", e);
//             return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error adding student: " + e.getMessage());
//         }
//     }

//     // @GetMapping("/check-auth")
//     // public ResponseEntity<?> checkAuth(HttpServletRequest request) {
//     //     try {
//     //         String token = request.getHeader("Authorization").substring(7);
//     //         String email = jwtUtil.getEmailFromToken(token);
//     //         if (jwtUtil.isTokenValid(token, email)) {
//     //             Optional<Student> optionalStudent = studentRrepository.findByEmail(email);
//     //             if (optionalStudent.isPresent()) {
//     //                 return ResponseEntity.ok(optionalStudent.get());
//     //             } else {
//     //                 return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("User not found");
//     //             }
//     //         } else {
//     //             return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid token");
//     //         }
//     //     } catch (Exception e) {
//     //         logger.error("Error checking authentication", e);
//     //         return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error checking authentication");
//     //     }
//     // }

//      @PostMapping("/google-login")
//     public ResponseEntity<?> googleLogin(@RequestBody Map<String, String> payload) {
//         String idToken = payload.get("idToken");

//         try {
//             GoogleIdToken.Payload googleUser = googleAuthService.verifyGoogleToken(idToken);
//             String email = googleUser.getEmail();

//             Optional<Student> optionalStudent = studentRrepository.findByEmail(email);
//             Student student;
//             if (optionalStudent.isPresent()) {
//                 student = optionalStudent.get();
//             } else {
//                 // Register a new user if not already in the database
//                 student = new Student();
//                 student.setEmail(email);
//                 student.setName((String) googleUser.get("name"));
//                 student.setPassword(""); // No password for Google users
//                 student = studentRrepository.save(student);
//             }

//             String token = jwtUtil.generateToken(email);
//             return ResponseEntity.ok(Map.of("user", student, "token", token));
//         } catch (Exception e) {
//             logger.error("Invalid Google ID token", e);
//             return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid Google ID token");
//         }
//     }
// }
package millet.demo.controller;

import millet.demo.models.Student;
import millet.demo.repo.StudentRrepository;
import millet.demo.request.LoginRequest;
import millet.demo.request.LoginResponse;
import millet.demo.service.GoogleAuthService;
import millet.demo.util.JwtUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;

import jakarta.servlet.http.HttpServletRequest;

import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private static final Logger logger = LoggerFactory.getLogger(AuthController.class);

    @Autowired
    private StudentRrepository studentRrepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private GoogleAuthService googleAuthService;

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest loginRequest) {
        try {
            Optional<Student> optionalStudent = studentRrepository.findByEmail(loginRequest.getEmail());
            if (optionalStudent.isPresent()) {
                Student student = optionalStudent.get();
                if (passwordEncoder.matches(loginRequest.getPassword(), student.getPassword())) {
                    String token = jwtUtil.generateToken(student.getEmail());
                    LoginResponse loginResponse = new LoginResponse(token, student.getId(), student.getName(), student.getEmail(), student.getCollege());
                    return ResponseEntity.ok(loginResponse);
                } else {
                    return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid email or password");
                }
            } else {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid email or password");
            }
        } catch (Exception e) {
            logger.error("Error logging in", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error logging in");
        }
    }

    @PostMapping("/signup")
    public ResponseEntity<?> addStudent(@RequestBody Student student) {
        try {
            logger.info("Adding student: {}", student);
            student.setPassword(passwordEncoder.encode(student.getPassword()));
            Student savedStudent = studentRrepository.save(student);
            return ResponseEntity.ok(savedStudent);
        } catch (Exception e) {
            logger.error("Error adding student", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error adding student: " + e.getMessage());
        }
    }

    @PostMapping("/google-login")
    public ResponseEntity<?> googleLogin(@RequestBody Map<String, String> payload) {
        String idToken = payload.get("idToken");
    
        try {
            // Verify the Google ID token
            GoogleIdToken.Payload googleUser = googleAuthService.verifyGoogleToken(idToken);
            String email = googleUser.getEmail();
    
            // Check if the user already exists in your database
            Optional<Student> optionalStudent = studentRrepository.findByEmail(email);
            Student student;
    
            if (optionalStudent.isPresent()) {
                student = optionalStudent.get();
            } else {
                // Register a new user if not already in the database
                student = new Student();
                student.setEmail(email);
                student.setName((String) googleUser.get("name")); // Assuming 'name' is in the payload
                student.setPassword(""); // No password for Google users
                
                // Fetch profile photo URL if available
                String photoUrl = (String) googleUser.get("picture"); // Assuming 'picture' field contains photo URL
                student.setProfilePicture(photoUrl); // Set profile photo URL in Student entity
                
                student = studentRrepository.save(student);
            }
    
            // Generate a JWT token for the user
            String token = jwtUtil.generateToken(email);
    
            // Return user information (including profile photo URL) and token in the response
            return ResponseEntity.ok(Map.of("user", student, "token", token));
        } catch (Exception e) {
            logger.error("Invalid Google ID token", e);
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid Google ID token");
        }
    }
    

        @GetMapping("/check-auth")
    public ResponseEntity<?> checkAuth(HttpServletRequest request) {
        try {
            String token = request.getHeader("Authorization").substring(7);
            String email = jwtUtil.getEmailFromToken(token);
            if (jwtUtil.isTokenValid(token, email)) {
                Optional<Student> optionalStudent = studentRrepository.findByEmail(email);
                if (optionalStudent.isPresent()) {
                    return ResponseEntity.ok(optionalStudent.get());
                } else {
                    return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("User not found");
                }
            } else {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid token");
            }
        } catch (Exception e) {
            logger.error("Error checking authentication", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error checking authentication");
        }
    }

}
