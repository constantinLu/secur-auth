package ro.secur.auth.configuration;

import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import ro.secur.auth.exceptions.ResponseFactory;

@Configuration
public class ApplicationConfiguration {

    @Bean
    public ModelMapper getModelMapper() {
        return new ModelMapper();
    }

    @Bean
    public ResponseFactory getResponseFactory() {
        return new ResponseFactory();
    }

//    @Bean
//    public WebMvcConfigurer corsConfigurer() {
//        return new WebMvcConfigurer() {
//            @Override
//            public void addCorsMappings(CorsRegistry registry) {
//                registry.addMapping("/users").allowedOrigins("http://localhost:3000/");
//            }
//        };
}


