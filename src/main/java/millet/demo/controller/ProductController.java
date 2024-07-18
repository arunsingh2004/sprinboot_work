package millet.demo.controller;

import millet.demo.models.Product;
import millet.demo.models.Review;
import millet.demo.repo.ProductRepository;
import millet.demo.repo.ReviewRepository;
import org.bson.types.ObjectId;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
// import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartFile;

// import io.jsonwebtoken.io.IOException;

// import static org.mockito.Mockito.description;
import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;

import java.util.ArrayList;
// import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/product")
public class ProductController {

    private static final Logger logger = LoggerFactory.getLogger(ProductController.class);

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private ReviewRepository reviewRepository;
    @Autowired
    private Cloudinary cloudinary;

      @SuppressWarnings("unchecked")
    @PostMapping("/")
    public ResponseEntity<?> addProduct(@RequestParam("name") String name,
                                        @RequestParam("type") String type,
                                        @RequestParam("description") String description,
                                        @RequestParam("price") double price,
                                        @RequestParam("quantity") int quantity,
                                        @RequestParam("imageUrl") MultipartFile imageUrl,
                                        @RequestParam("additionalImages") List<MultipartFile> additionalImages,
                                        @RequestParam("details") String details) {
        try {
            logger.info("Adding product: {}", name);

            // Upload the main image to Cloudinary
            Map<String, Object> uploadResult = cloudinary.uploader().upload(imageUrl.getBytes(), ObjectUtils.asMap(
                "public_id", UUID.randomUUID().toString()));
            String mainImageUrl = (String) uploadResult.get("url");

            // Upload additional images to Cloudinary
            List<String> additionalImageUrls = new ArrayList<>();
            for (MultipartFile file : additionalImages) {
                uploadResult = cloudinary.uploader().upload(file.getBytes(), ObjectUtils.asMap(
                    "public_id", UUID.randomUUID().toString()));
                additionalImageUrls.add((String) uploadResult.get("url"));
            }

            // Create a new Product object
            Product product = new Product();
            product.setName(name);
            product.settype(type);
            product.setDescription(description);
            product.setPrice(price);
            product.setQuantity(quantity);
            product.setImageUrl(mainImageUrl);
            product.setAdditionalImages(additionalImageUrls);
            product.setDetails(details);

            // Save the product to the database
            Product savedProduct = productRepository.save(product);
            return ResponseEntity.ok(savedProduct);
        } catch (Exception e) {
            logger.error("Error adding product", e);
            return ResponseEntity.status(500).body("Error adding product: " + e.getMessage());
        }
    }
    @GetMapping("/")
    public ResponseEntity<?> getProducts() {
        try {
            return ResponseEntity.ok(productRepository.findAll());
        } catch (Exception e) {
            logger.error("Error fetching products", e);
            return ResponseEntity.status(500).body("Error fetching products: " + e.getMessage());
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getProductById(@PathVariable String id) {
        try {
            ObjectId objectId = new ObjectId(id);
            Optional<Product> product = productRepository.findById(objectId);
            if (product.isPresent()) {
                return ResponseEntity.ok(product.get());
            } else {
                return ResponseEntity.status(404).body("Product not found");
            }
        } catch (IllegalArgumentException e) {
            logger.error("Invalid ID format", e);
            return ResponseEntity.status(400).body("Invalid ID format: " + id);
        } catch (Exception e) {
            logger.error("Error fetching product", e);
            return ResponseEntity.status(500).body("Error fetching product: " + e.getMessage());
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateProduct(@PathVariable String id,
                                           @RequestParam("name") String name,
                                           @RequestParam("type") String type,
                                           @RequestParam("description") String description,
                                           @RequestParam("price") double price,
                                           @RequestParam("quantity") int quantity,
                                           @RequestParam(value = "imageUrl", required = false) MultipartFile imageUrl,
                                           @RequestParam(value = "additionalImages", required = false) List<MultipartFile> additionalImages,
                                           @RequestParam("details") String details) {
        try {
            ObjectId objectId = new ObjectId(id);
            Optional<Product> optionalProduct = productRepository.findById(objectId);
            if (optionalProduct.isPresent()) {
                Product existingProduct = optionalProduct.get();
                existingProduct.setName(name);
                existingProduct.settype(type);
                existingProduct.setDescription(description);
                existingProduct.setPrice(price);
                existingProduct.setQuantity(quantity);
                existingProduct.setDetails(details);

                if (imageUrl != null) {
                    @SuppressWarnings("unchecked")
                    Map<String, Object> uploadResult = cloudinary.uploader().upload(imageUrl.getBytes(), ObjectUtils.asMap(
                        "public_id", UUID.randomUUID().toString()));
                    existingProduct.setImageUrl((String) uploadResult.get("url"));
                }

                if (additionalImages != null && !additionalImages.isEmpty()) {
                    List<String> additionalImageUrls = new ArrayList<>();
                    for (MultipartFile file : additionalImages) {
                        @SuppressWarnings("unchecked")
                        Map<String, Object> uploadResult = cloudinary.uploader().upload(file.getBytes(), ObjectUtils.asMap(
                            "public_id", UUID.randomUUID().toString()));
                        additionalImageUrls.add((String) uploadResult.get("url"));
                    }
                    existingProduct.setAdditionalImages(additionalImageUrls);
                }

                productRepository.save(existingProduct);
                return ResponseEntity.ok(existingProduct);
            } else {
                return ResponseEntity.status(404).body("Product not found");
            }
        } catch (IllegalArgumentException e) {
            logger.error("Invalid ID format", e);
            return ResponseEntity.status(400).body("Invalid ID format: " + id);
        } catch (Exception e) {
            logger.error("Error updating product", e);
            return ResponseEntity.status(500).body("Error updating product: " + e.getMessage());
        }
    }
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteProduct(@PathVariable String id) {
        try {
            ObjectId objectId = new ObjectId(id);
            productRepository.deleteById(objectId);
            return ResponseEntity.ok("Product deleted with id: " + id);
        } catch (IllegalArgumentException e) {
            logger.error("Invalid ID format", e);
            return ResponseEntity.status(400).body("Invalid ID format: " + id);
        } catch (Exception e) {
            logger.error("Error deleting product", e);
            return ResponseEntity.status(500).body("Error deleting product: " + e.getMessage());
        }
    }

    @PostMapping("/{id}/review")
    public ResponseEntity<?> addReview(@PathVariable String id, @RequestBody Review review) {
        try {
            ObjectId objectId = new ObjectId(id);
            Optional<Product> optionalProduct = productRepository.findById(objectId);
            if (optionalProduct.isPresent()) {
                Product product = optionalProduct.get();
                review.setProductId(id);
                Review savedReview = reviewRepository.save(review);
                product.addReview(savedReview);
                productRepository.save(product);
                return ResponseEntity.ok(savedReview);
            } else {
                return ResponseEntity.status(404).body("Product not found");
            }
        } catch (IllegalArgumentException e) {
            logger.error("Invalid ID format", e);
            return ResponseEntity.status(400).body("Invalid ID format: " + id);
        } catch (Exception e) {
            logger.error("Error adding review", e);
            return ResponseEntity.status(500).body("Error adding review: " + e.getMessage());
        }
    }

    @GetMapping("/{id}/reviews")
    public ResponseEntity<?> getReviews(@PathVariable String id) {
        try {
            List<Review> reviews = reviewRepository.findByProductId(id);
            return ResponseEntity.ok(reviews);
        } catch (Exception e) {
            logger.error("Error fetching reviews", e);
            return ResponseEntity.status(500).body("Error fetching reviews: " + e.getMessage());
        }
    }
    @GetMapping("/reviews")
    public ResponseEntity<?> getReview() {
        try {
            // List<Review> reviews = reviewRepository.findAll(id);
            return ResponseEntity.ok(reviewRepository.findAll());
        } catch (Exception e) {
            logger.error("Error fetching reviews", e);
            return ResponseEntity.status(500).body("Error fetching reviews: " + e.getMessage());
        }
    }
}
