package ro.secur.auth.security.filter;


import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.Jwts;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import ro.secur.auth.configuration.JwtConfiguration;
import ro.secur.auth.security.authentication.AuthenticationRequest;

import javax.servlet.FilterChain;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.sql.Date;
import java.time.LocalDate;
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

//        try {
//            StringBuilder stringBuilder = new StringBuilder();
//            InputStream inputstream = request.getInputStream();
//            BufferedReader br = new BufferedReader(new InputStreamReader(inputstream));
//
//            String s = null;
//            StringBuilder sb = new StringBuilder();
//            while ((s = br.readLine()) != null) {
//                stringBuilder.append(s);
//            }
//            System.out.println(stringBuilder.toString());
//
//        } catch (Exception e) {
//
//        }
        try {


            AuthenticationRequest authenticationRequest = new ObjectMapper().readValue(request.getInputStream(), AuthenticationRequest.class);


            Authentication authentication = new UsernamePasswordAuthenticationToken(
                    authenticationRequest.getUsername(),
                    authenticationRequest.getPassword()
            );
            return authenticationManager.authenticate(authentication);

        } catch (IOException e) {
            logger.info("Authentication not possible with the current credentials.");
            // TODO SEE WHAT CAN WE CUSTOMIZE THIS ERROR. (errormapping)
            throw new RuntimeException(e);
        }
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain
            chain, Authentication authResult) {

        String token = Jwts.builder()
                .setSubject(authResult.getName())
                .claim(ROLES, authResult.getAuthorities())
                .setExpiration(Date.valueOf(String.valueOf(LocalDateTime.now().plusMinutes(jwtConfiguration.getTokenExpirationDays()))))
                .signWith(jwtConfiguration.secretKey())
                .compact();

        response.addHeader(jwtConfiguration.getAuthorizationHeader(), jwtConfiguration.getTokenPrefix() + token);
    }
}














































































































































































































































































































































