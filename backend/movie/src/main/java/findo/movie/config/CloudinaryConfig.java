package findo.movie.config;

import com.cloudinary.Cloudinary;
import java.util.HashMap;
import java.util.Map;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class CloudinaryConfig {
    
    private final String CLOUD_NAME = "dn0xfy6bu";
    private final String API_KEY = "859189834198491";
    private final String API_SECRET = "xldEtBVQVJXNREHuUK3k8JLr4LQ";

    @Bean
    public Cloudinary cloudinary() {
        Map<String, String> config = new HashMap<>();
        config.put("cloud_name", CLOUD_NAME);
        config.put("api_key", API_KEY);
        config.put("api_secret", API_SECRET);
        return new Cloudinary(config);
    }
}
