package ro.secur.auth.configuration;

import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ApplicationConfiguration {

    @Bean
    public ModelMapper getModelMapper() {
        return new ModelMapper();
    }
}

