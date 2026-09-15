package com.cue.demo.security;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Service
public final class JwtService {

    @Value("${security.jwt.secret-key}")
    private String secretKey;

    /**
     * Generates a JWT token for a given user, valid for 24 hours.
     *
     * @param userDetails the user details for whom the token is generated.
     * @return the generated JWT token.
     */
    public String generateToken(final UserDetails userDetails) {

        return Jwts.builder()
                .claims(buildRoleClaims(userDetails))
                .subject(userDetails.getUsername())
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60 * 24))
                .signWith(getSignInKey())
                .compact();
    }

    public boolean isValidToken(final String token, final UserDetails userDetails) {
        final String username = extractUsername(token);
        return (username.equals(userDetails.getUsername())) && !isTokenExpired(token);
    }

    /**
     * Decodes the secret key and returns a SecretKey object for signing tokens.
     *
     * @return the signing key.
     */
    private SecretKey getSignInKey() {
        byte[] keyBytes = Decoders.BASE64.decode(secretKey);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    private boolean isTokenExpired(final String token) {
        return extractExpiration(token).before(new Date());
    }

    private Date extractExpiration(final String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    public <T> T extractClaim(final String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    /**
     * Parses the token and extracts the complete payload.
     *
     * @param token jwt token formatted as a String to be parsed.
     * @return All Claims
     */
    private Claims extractAllClaims(final String token) {
        return Jwts.parser()
                .verifyWith(getSignInKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    /**
     * Helper method.
     * Extracts the role claims from a userDetails object
     * @param userDetails UserDetails interface.
     * @return A Map<String, Object> used as a parameter for the Jwts.builder().claims() argument
     */
    private Map<String, Object> buildRoleClaims(final UserDetails userDetails) {
        Collection<? extends GrantedAuthority> authorities = userDetails.getAuthorities();
        String roleClaim = authorities.iterator().next().getAuthority();
        Map<String, Object> roleClaims = new HashMap<>();
        roleClaims.put("role", roleClaim);
        return roleClaims;
    }
}
