package ro.secur.auth.security.filter;


import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.Jwts;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import ro.secur.auth.configuration.JwtConfiguration;
import ro.secur.auth.exceptions.custom.AuthException;
import ro.secur.auth.exceptions.custom.InputStreamException;
import ro.secur.auth.security.authentication.AuthenticationRequest;
import ro.secur.auth.util.DateUtil;

import javax.servlet.FilterChain;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.LocalDateTime;

import static ro.secur.auth.common.Commons.ROLES;
import static ro.secur.auth.util.Api.LOGIN_URL;

public class AuthenticationFilter extends UsernamePasswordAuthenticationFilter {

    private final AuthenticationManager authenticationManager;

    private final JwtConfiguration jwtConfiguration;

    public AuthenticationFilter(AuthenticationManager authenticationManager, JwtConfiguration jwtConfiguration) {
        this.authenticationManager = authenticationManager;
        this.jwtConfiguration = jwtConfiguration;
        setFilterProcessesUrl(LOGIN_URL);
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {

        try {

            AuthenticationRequest authenticationRequest = new ObjectMapper().readValue(request.getInputStream(), AuthenticationRequest.class);

            Authentication authentication = new UsernamePasswordAuthenticationToken(
                    authenticationRequest.getUsername(),
                    authenticationRequest.getPassword()
            );
            return authenticationManager.authenticate(authentication);

        } catch (AuthenticationException e) {
            logger.info("Authentication not possible with the current credentials.");
            throw new AuthException(e.getMessage());
        } catch (IOException e) {
            logger.info("Request from input stream not valid.");
            throw new InputStreamException(e.getMessage());
        }
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain
            chain, Authentication authResult) {

        String token = Jwts.builder()
                .setSubject(authResult.getName())
                .claim(ROLES, authResult.getAuthorities())
                //.setExpiration(Date.valueOf(LocalDate.now().plusDays(jwtConfiguration.getTokenExpirationDays())))
                //TODO: TESTING PURPOSES. DELETE AFTER
                .setExpiration(DateUtil.asDate((LocalDateTime.now()).plusMinutes(jwtConfiguration.getTokenExpirationDays())))
                .signWith(jwtConfiguration.secretKey())
                .compact();

        response.addHeader(jwtConfiguration.getAuthorizationHeader(), jwtConfiguration.getTokenPrefix() + token);
    }
}