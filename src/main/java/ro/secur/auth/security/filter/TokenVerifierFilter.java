package ro.secur.auth.security.filter;

import com.google.common.base.Strings;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import org.apache.commons.lang.StringUtils;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.preauth.PreAuthenticatedAuthenticationToken;
import org.springframework.web.filter.OncePerRequestFilter;
import ro.secur.auth.configuration.JwtConfiguration;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import static ro.secur.auth.common.Commons.*;


public class TokenVerifierFilter extends OncePerRequestFilter {

    private JwtConfiguration jwtConfiguration;

    public TokenVerifierFilter(JwtConfiguration jwtConfiguration) {
        this.jwtConfiguration = jwtConfiguration;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        String authorizationHeader = request.getHeader(jwtConfiguration.getAuthorizationHeader());
        if (Strings.isNullOrEmpty(authorizationHeader) || !authorizationHeader.startsWith(jwtConfiguration.getTokenPrefix())) {
            filterChain.doFilter(request, response);
            // the request will be rejected
            return;
        }

        String token = authorizationHeader.replace(jwtConfiguration.getTokenPrefix(), StringUtils.EMPTY);

        try {

            Authentication authentication = getAuthenticated(token);

            SecurityContextHolder.getContext().setAuthentication(authentication);

        } catch (JwtException e) {
            throw new JwtException(String.format("Token %s not valid ! ", token));
        }
    }

    private Authentication getAuthenticated(String token) {
        Jws<Claims> claimsJws = Jwts.parser()
                .setSigningKey(jwtConfiguration.secretKey())
                .parseClaimsJws(token);

        Claims body = claimsJws.getBody();
        String username = body.getSubject();
        var authorities = (List<Map<Strings, String>>) body.get(ROLES);
        Set<SimpleGrantedAuthority> simpleGrantedAuthority = authorities.stream()
                .map(m -> new SimpleGrantedAuthority(m.get(AUTHORITY)))
                .collect(Collectors.toSet());

        return new PreAuthenticatedAuthenticationToken(username, null, simpleGrantedAuthority);
    }
}
