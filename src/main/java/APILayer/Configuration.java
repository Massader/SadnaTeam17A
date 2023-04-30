package APILayer;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.core.convert.converter.Converter;

import java.util.UUID;

@org.springframework.context.annotation.Configuration
@ComponentScan(basePackages = {"APILayer", "ServiceLayer"})
public class Configuration {

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


}
