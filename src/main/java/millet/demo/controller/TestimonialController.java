package millet.demo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import millet.demo.models.Testimonial;
import millet.demo.service.TestimonialService;
import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;

import java.io.IOException;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/testimonials")
public class TestimonialController {
    @Autowired
    private TestimonialService testimonialService;

    @Autowired
    private Cloudinary cloudinary;

    @GetMapping("/")
    public List<Testimonial> getAllTestimonials() {
        return testimonialService.getAllTestimonials();
    }
    @PostMapping("/")
    public Testimonial createTestimonial(@RequestParam("name") String name,
                                         @RequestParam("message") String message,
                                         @RequestParam(value = "profilepic") MultipartFile profilepic,
                                         @RequestParam(value = "image", required = false) MultipartFile image,
                                         @RequestParam(value = "videoUrl", required = false) String videoUrl) throws IOException {
        String profilepicUrl =uploadToCloudinary(profilepic);
        String imageUrl = image != null ? uploadToCloudinary(image) : null;

        Testimonial testimonial = new Testimonial();
        testimonial.setName(name);
        testimonial.setMessage(message);
        testimonial.setProfilepic(profilepicUrl);
        testimonial.setImageUrl(imageUrl);
        testimonial.setVideoUrl(videoUrl);

        return testimonialService.saveTestimonial(testimonial);
    }

    private String uploadToCloudinary(MultipartFile file) throws IOException {
        @SuppressWarnings("rawtypes")
        Map uploadResult = cloudinary.uploader().upload(file.getBytes(), ObjectUtils.emptyMap());
        return (String) uploadResult.get("url");
    }
    
}
