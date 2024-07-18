package millet.demo.config;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Map;

@Configuration
public class CloudinaryConfig {

    @Bean
    public Cloudinary cloudinary() {
        @SuppressWarnings("unchecked")
      Map<String, String> config = ObjectUtils.asMap(
                "cloud_name", "dhdfqd4h0",
                "api_key", "629528237381531",
                "api_secret", "gZw2WTsm6tYGyrQYcAd5ymUaBT4"
        );
        return new Cloudinary(config);
    }
}
