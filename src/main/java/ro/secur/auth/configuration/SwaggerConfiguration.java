package ro.secur.auth.configuration;

import com.fasterxml.classmate.TypeResolver;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import ro.secur.auth.security.authentication.AuthenticationRequest;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.ApiKey;
import springfox.documentation.service.AuthorizationScope;
import springfox.documentation.service.Contact;
import springfox.documentation.service.SecurityReference;
import springfox.documentation.spi.service.contexts.SecurityContext;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import static io.swagger.models.auth.In.HEADER;
import static java.util.Collections.singletonList;
import static javax.ws.rs.core.HttpHeaders.AUTHORIZATION;
import static springfox.documentation.spi.DocumentationType.SWAGGER_2;


@Configuration
@EnableSwagger2
public class SwaggerConfiguration {

    @Bean
    public Docket api(TypeResolver typeResolver) {
        return new Docket(SWAGGER_2)
                .apiInfo(apiInfo())
                .securitySchemes(singletonList(new ApiKey("JWT", AUTHORIZATION, HEADER.name())))
                .securityContexts(singletonList(
                        SecurityContext.builder()
                                .securityReferences(
                                        singletonList(SecurityReference.builder()
                                                .reference("JWT")
                                                .scopes(new AuthorizationScope[0])
                                                .build()
                                        )
                                )
                                .build())
                )
                .additionalModels(typeResolver.resolve(AuthenticationRequest.class))
                .select()
                .apis(RequestHandlerSelectors.basePackage("ro.secur.auth"))
                .build();
    }


    private ApiInfo apiInfo() {
        Contact contact = new Contact("Fortech Secur", "https://github.com/gizet/secur-auth", "fortechSecur@gmail.com");
        return new ApiInfoBuilder()
                .title("Secur App REST API")
                .description("List of available API served by secur App")
                .version("1.0")
                .license("None")
                .licenseUrl("")
                .contact(contact)
                .build();
    }
}