package millet.demo.controller;

import millet.demo.models.BannerAd;
import millet.demo.service.BannerAdService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@RestController
@RequestMapping("/api/bannerAds")
public class BannerAdController {
    @Autowired
    private BannerAdService bannerAdService;

    @Autowired
    private Cloudinary cloudinary;

    private static final Logger logger = LoggerFactory.getLogger(BannerAdController.class);

    @PostMapping
    public ResponseEntity<?> uploadBannerAd(
            @RequestParam("type") String type,
            @RequestParam("title") String title,
            @RequestParam("description") String description,
            @RequestParam("image") MultipartFile image) throws IOException {

        try {
            @SuppressWarnings("rawtypes")
            Map uploadResult = cloudinary.uploader().upload(image.getBytes(),
                    ObjectUtils.asMap("public_id", UUID.randomUUID().toString()));

            BannerAd bannerAd = new BannerAd();
            bannerAd.settype(type);
            bannerAd.settitle(title);
            bannerAd.setdescription(description);
            bannerAd.setimageurl((String) uploadResult.get("url"));

            BannerAd savedBannerAd = bannerAdService.saveBannerAd(bannerAd);
            return ResponseEntity.ok(savedBannerAd);
        } catch (IOException e) {
            logger.error("Failed to upload image", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to save image");
        }
    }

    @GetMapping
    public ResponseEntity<?> getAllBannerAds() {
        try {
            List<BannerAd> bannerAds = bannerAdService.getAllBannerAds();
            return ResponseEntity.ok(bannerAds);
        } catch (Exception e) {
            logger.error("Error getting banners", e);
            return ResponseEntity.status(500).body("Error getting banners");
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getBannerAds(@PathVariable String id) {
        try {
            Optional<BannerAd> bannerAds = bannerAdService.getBannerAdById(id);
            return ResponseEntity.ok(bannerAds);
        } catch (Exception e) {
            logger.error("Error getting banner", e);
            return ResponseEntity.status(500).body("Error getting banner");
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteBannerAd(@PathVariable String id) {
        try {
            bannerAdService.deleteBannerAd(id);
            return ResponseEntity.ok("Banner deleted");
        } catch (Exception e) {
            logger.error("Error in deleting banner", e);
            return ResponseEntity.status(500).body("Error in deleting banner");
        }
    }

    @PatchMapping("/{id}")
    public ResponseEntity<?> updateBannerAds(
            @PathVariable String id,
            @RequestParam(value = "title", required = false) String title,
            @RequestParam(value = "description", required = false) String description,
            @RequestParam(value = "type", required = false) String type,
            @RequestParam(value = "image", required = false) MultipartFile image) throws IOException {

        try {
            Optional<BannerAd> optionalBannerAd = bannerAdService.getBannerAdById(id);
            if (!optionalBannerAd.isPresent()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("BannerAd not found");
            }

            BannerAd exisBannerAd = optionalBannerAd.get();
            if (title != null) {
                exisBannerAd.settitle(title);
            }
            if (type != null) {
                exisBannerAd.settype(type);
            }
            if (description != null) {
                exisBannerAd.setdescription(description);
            }
            if (image != null) {
                @SuppressWarnings("rawtypes")
                Map uploadResult = cloudinary.uploader().upload(image.getBytes(),
                        ObjectUtils.asMap("public_id", UUID.randomUUID().toString()));
                exisBannerAd.setimageurl((String) uploadResult.get("url"));
            }

            ResponseEntity<?> updatedBannerAd = bannerAdService.updateBannerAd(id, exisBannerAd);
            return ResponseEntity.ok(updatedBannerAd);
        } catch (Exception e) {
            logger.error("Error in updating banner", e);
            return ResponseEntity.status(500).body("Error in updating banner" + e);
        }
    }
}
