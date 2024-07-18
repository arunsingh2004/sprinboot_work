package millet.demo.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import millet.demo.models.Testimonial;
import millet.demo.repo.TestimonialRepository;

import java.util.List;

@Service
public class TestimonialService {
    @Autowired
    private TestimonialRepository testimonialRepository;

    public List<Testimonial> getAllTestimonials() {
        return testimonialRepository.findAll();
    }

    public Testimonial saveTestimonial(Testimonial testimonial) {
        return testimonialRepository.save(testimonial);
    }
}
