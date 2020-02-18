package ro.secur.auth.configuration;

import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import ro.secur.auth.exceptions.FactoryResponse;

@Configuration
public class ApplicationConfiguration {

    @Bean
    public ModelMapper getModelMapper() {
        return new ModelMapper();
    }

    @Bean
    public FactoryResponse getResponseCreator() {
        return new FactoryResponse();
    }
}

