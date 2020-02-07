package ro.secur.auth.security.authentication;

import io.jsonwebtoken.Jwts;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import ro.secur.auth.security.jwt.JwtConfig;

import java.time.LocalDate;
import java.util.Date;

@Service
public class AuthService {

    private AuthenticationManager authenticationManager;

    private final JwtConfig jwtConfig;

    public AuthService(AuthenticationManager authenticationManager, JwtConfig jwtConfig) {
        this.authenticationManager = authenticationManager;
        this.jwtConfig = jwtConfig;
    }

    public Authentication authenticate(AuthenticationRequest request) {
        return authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword()));
    }

    public String generateToken(Authentication authentication) {
        return Jwts.builder().setSubject(authentication.getName())
                .claim("roles", authentication.getAuthorities())
                .setIssuedAt(new Date())
                .setExpiration(java.sql.Date.valueOf(LocalDate.now().plusWeeks(jwtConfig.getTokenExpirationDays())))
                .signWith(jwtConfig.secretKey())
                .compact();
    }
}
