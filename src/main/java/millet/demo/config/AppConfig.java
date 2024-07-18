package millet.demo.config;

import com.fasterxml.jackson.databind.module.SimpleModule;
import millet.demo.util.ObjectIdSerializer;
import org.bson.types.ObjectId;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;

@Configuration
public class AppConfig {

    @Bean
    public Jackson2ObjectMapperBuilder jacksonBuilder() {
        Jackson2ObjectMapperBuilder builder = new Jackson2ObjectMapperBuilder();
        SimpleModule module = new SimpleModule();
        module.addSerializer(ObjectId.class, new ObjectIdSerializer());
        builder.modules(module);
        return builder;
    }
}
