package millet.demo.controller;

import millet.demo.models.AboutPage;
import millet.demo.repo.AboutPageRepository;

import org.bson.types.ObjectId;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.Optional;
import java.util.Map;
import java.util.UUID;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;

@RestController
@RequestMapping("/about")
public class AboutPageController {

    private static final Logger logger = LoggerFactory.getLogger(AboutPageController.class);

    @Autowired
    private AboutPageRepository aboutPageRepository;

    @Autowired
    private Cloudinary cloudinary;

    @PostMapping("/")
    public ResponseEntity<?> createAboutPage(@RequestParam("title") String title,
                                             @RequestParam("description") String description,
                                             @RequestParam("journeyHeading") String journeyHeading,
                                             @RequestParam("journeyContent") String journeyContent,
                                             @RequestParam("promisesHeading") String promisesHeading,
                                             @RequestParam("promisesContent") String promisesContent,
                                             @RequestParam("ingredientHeading") String ingredientHeading,
                                             @RequestParam("ingredientContent") String ingredientContent,
                                             @RequestParam("banner") MultipartFile banner) {
        try {
            @SuppressWarnings("unchecked")
            Map<String, Object> uploadResult = cloudinary.uploader().upload(banner.getBytes(),
                    ObjectUtils.asMap("public_id", UUID.randomUUID().toString()));

            AboutPage aboutPage = new AboutPage();
            aboutPage.setTitle(title);
            aboutPage.setDescription(description);
            aboutPage.setJourney(new AboutPage.Section(journeyHeading, journeyContent));
            aboutPage.setPromises(new AboutPage.Section(promisesHeading, promisesContent));
            aboutPage.setIngredient(new AboutPage.Section(ingredientHeading, ingredientContent));
            aboutPage.setBanner((String) uploadResult.get("url"));

            logger.info("Creating About Page: {}", aboutPage);
            AboutPage savedAboutPage = aboutPageRepository.save(aboutPage);
            return ResponseEntity.ok(savedAboutPage);
        } catch (Exception e) {
            logger.error("Error creating About Page", e);
            return ResponseEntity.status(500).body("Error creating About Page: " + e.getMessage());
        }
    }

    @GetMapping("/")
    public ResponseEntity<?> getAboutPage() {
        try {
            return ResponseEntity.ok(aboutPageRepository.findAll());
        } catch (Exception e) {
            logger.error("Error fetching About Page", e);
            return ResponseEntity.status(500).body("Error fetching About Page: " + e.getMessage());
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateAboutPage(@PathVariable String id,
                                             @RequestParam("title") String title,
                                             @RequestParam("description") String description,
                                             @RequestParam("journeyHeading") String journeyHeading,
                                             @RequestParam("journeyContent") String journeyContent,
                                             @RequestParam("promisesHeading") String promisesHeading,
                                             @RequestParam("promisesContent") String promisesContent,
                                             @RequestParam("ingredientHeading") String ingredientHeading,
                                             @RequestParam("ingredientContent") String ingredientContent,
                                             @RequestParam(value = "banner", required = false) MultipartFile banner) {
        try {
            ObjectId objectId = new ObjectId(id);
            Optional<AboutPage> optionalAbout = aboutPageRepository.findById(objectId);
            if (optionalAbout.isPresent()) {
                AboutPage aboutPageToUpdate = optionalAbout.get();
                aboutPageToUpdate.setTitle(title);
                aboutPageToUpdate.setDescription(description);
                aboutPageToUpdate.setJourney(new AboutPage.Section(journeyHeading, journeyContent));
                aboutPageToUpdate.setPromises(new AboutPage.Section(promisesHeading, promisesContent));
                aboutPageToUpdate.setIngredient(new AboutPage.Section(ingredientHeading, ingredientContent));

                if (banner != null) {
                    @SuppressWarnings("unchecked")
                    Map<String, Object> uploadResult = cloudinary.uploader().upload(banner.getBytes(),
                            ObjectUtils.asMap("public_id", UUID.randomUUID().toString()));
                    aboutPageToUpdate.setBanner((String) uploadResult.get("url"));
                }

                AboutPage updatedAboutPage = aboutPageRepository.save(aboutPageToUpdate);
                return ResponseEntity.ok(updatedAboutPage);
            } else {
                return ResponseEntity.status(404).body("About Page not found");
            }
        } catch (Exception e) {
            logger.error("Error updating About Page", e);
            return ResponseEntity.status(500).body("Error updating About Page: " + e.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteAboutPage(@PathVariable String id) {
        try {
            ObjectId objectId = new ObjectId(id);
            aboutPageRepository.deleteById(objectId);
            return ResponseEntity.ok("About Page deleted");
        } catch (Exception e) {
            logger.error("Error deleting About Page", e);
            return ResponseEntity.status(500).body("Error deleting About Page: " + e.getMessage());
        }
    }
}
