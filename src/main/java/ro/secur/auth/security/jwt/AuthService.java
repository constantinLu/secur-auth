package ro.secur.auth.security.jwt;

import io.jsonwebtoken.Jwts;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;
import ro.secur.auth.security.authentication.AuthenticationRequest;

import java.time.LocalDate;
import java.util.Date;

@Component
public class AuthService {

    private AuthenticationManager authenticationManager;

    private final JwtUtil jwtUtil;

    public AuthService(AuthenticationManager authenticationManager, JwtUtil jwtUtil) {
        this.authenticationManager = authenticationManager;
        this.jwtUtil = jwtUtil;
    }

    public Authentication authenticate(AuthenticationRequest request) {
        return authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword()));
    }

    public String generateToken(Authentication authentication) {

        return Jwts.builder().setSubject(authentication.getName())
                .setIssuedAt(new Date())
                .setExpiration(java.sql.Date.valueOf(LocalDate.now().plusWeeks(jwtUtil.getTokenExpirationDays())))
                .signWith(jwtUtil.secretKey())
                .compact();
    }
}
