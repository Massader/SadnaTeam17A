package APILayer;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.core.convert.converter.Converter;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.stereotype.Repository;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.UUID;

@org.springframework.context.annotation.Configuration
@ComponentScan(basePackages = {"APILayer", "ServiceLayer", "DomainLayer", "DataAccessLayer"})
@EntityScan("DomainLayer")
@EnableJpaRepositories("DataAccessLayer")


public class Configuration implements WebMvcConfigurer {

    @Bean
    public Converter<String, UUID> UUIDToStringConverter() {
        return new Converter<String, UUID>() {
            @Override
            public UUID convert(String source) {
                return UUID.fromString(source);
            }
        };
    }

    @Bean
    public Converter<UUID, String> StringToUUIDConverter() {
        return new Converter<UUID, String>() {
            @Override
            public String convert(UUID source) {
                return source.toString();
            }
        };
    }

    @Bean
    public UUID uuid() {
        return UUID.randomUUID();
    }

    @Bean
    public String string() {
        return "Hello, world!";
    }

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                .allowedOrigins("*")
                .allowedMethods("GET", "POST", "PUT", "DELETE")
                .allowedHeaders("*")
                .maxAge(3600);
    }
    
    @Bean
    public ObjectMapper objectMapper() {
        return new ObjectMapper();
    }
    
    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }
}
