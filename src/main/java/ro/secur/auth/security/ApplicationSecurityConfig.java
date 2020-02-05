package ro.secur.auth.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.password.PasswordEncoder;
import ro.secur.auth.security.jwt.SecurJwtConfig;
import ro.secur.auth.security.jwt.SecurJwtSecretKey;
import ro.secur.auth.security.jwt.SecurUsernameAndPasswordAuthenticationFiler;
import ro.secur.auth.service.UserManagementService;

@Configuration
@EnableWebSecurity
public class ApplicationSecurityConfig extends WebSecurityConfigurerAdapter {

    private final UserManagementService userManagementService;
    private final PasswordEncoder passwordEncoder;
    private final SecurJwtSecretKey secretKey;
    private final SecurJwtConfig jwtConfig;

    public ApplicationSecurityConfig(UserManagementService userManagementService, PasswordEncoder passwordEncoder, SecurJwtSecretKey secretKey, SecurJwtConfig jwtConfig) {
        this.userManagementService = userManagementService;
        this.passwordEncoder = passwordEncoder;
        this.secretKey = secretKey;
        this.jwtConfig = jwtConfig;
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .csrf().disable()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .addFilter(new SecurUsernameAndPasswordAuthenticationFiler(authenticationManager(), secretKey, jwtConfig));
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.authenticationProvider(daoAuthenticationProvider());
    }

    @Bean
    public DaoAuthenticationProvider daoAuthenticationProvider() {
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setPasswordEncoder(passwordEncoder);
        provider.setUserDetailsService(userManagementService);
        return provider;
    }
}
