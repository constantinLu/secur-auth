package ro.secur.auth.configuration;


import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.BeanIds;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import ro.secur.auth.security.filter.AuthenticationFilter;
import ro.secur.auth.security.filter.TokenVerifierFilter;
import ro.secur.auth.service.UserService;

import static ro.secur.auth.common.Role.ADMIN;
import static ro.secur.auth.util.Api.*;

@Configuration
@EnableWebSecurity
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {

    private final UserService userService;

    private final JwtConfiguration jwtConfiguration;

    private final PasswordConfiguration passwordConfiguration;

    public SecurityConfiguration(UserService userService, JwtConfiguration jwtConfiguration, PasswordConfiguration passwordConfiguration) {
        this.userService = userService;
        this.jwtConfiguration = jwtConfiguration;
        this.passwordConfiguration = passwordConfiguration;
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .cors().and()
                .csrf().disable()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .authorizeRequests()
                .antMatchers(FORGOT_PASSWORD_URL).permitAll()
                .antMatchers(RESET_PASSWORD_URL).permitAll()
                .and()
                .addFilter(new AuthenticationFilter(authenticationManager(), jwtConfiguration))
                .addFilterAfter(new TokenVerifierFilter(jwtConfiguration), AuthenticationFilter.class)
                .authorizeRequests()
                .antMatchers(USERS_URL).hasRole(ADMIN.toString())
                .anyRequest()
                .authenticated();
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.authenticationProvider(daoAuthenticationProvider());
    }

    @Bean
    public DaoAuthenticationProvider daoAuthenticationProvider() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setPasswordEncoder(passwordConfiguration.getEncoder());
        authProvider.setUserDetailsService(userService);
        return authProvider;
    }


    @Bean(name = BeanIds.AUTHENTICATION_MANAGER)
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }
}
