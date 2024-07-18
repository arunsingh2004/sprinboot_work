package millet.demo.util;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.function.Function;

@Component
public class JwtUtil {

    private static final String SECRET_KEY = "bjkjvasdldjfberpurbgeqjbqeobvveoqkvmqod66+5"; // Ensure this is a sufficiently long secret key

    // Convert String to SecretKey
    private SecretKey getSigningKey() {
        return Keys.hmacShaKeyFor(SECRET_KEY.getBytes());
    }

    public String generateToken(String email) {
        return Jwts.builder()
                .setSubject(email)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60 * 10))
                .signWith(getSigningKey(), SignatureAlgorithm.HS256) // Updated method
                .compact();
    }

    public String getEmailFromToken(String token) {
        return getClaimFromToken(token, Claims::getSubject);
    }

    public <T> T getClaimFromToken(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = getAllClaimsFromToken(token);
        return claimsResolver.apply(claims);
    }

    private Claims getAllClaimsFromToken(String token) {
        return Jwts.parserBuilder() // Updated to parserBuilder()
                .setSigningKey(getSigningKey()) // Updated method
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    public Boolean isTokenValid(String token,String email) {
        final String getEmailFromToken = getEmailFromToken(token);
        return (getEmailFromToken.equals(email) && !isTokenExpired(token));
    }

    private Boolean isTokenExpired(String token) {
        final Date expiration = getExpirationDateFromToken(token);
        return expiration.before(new Date());
    }

    public Date getExpirationDateFromToken(String token) {
        return getClaimFromToken(token, Claims::getExpiration);
    }

    public boolean validateJwtToken(String jwt) {
        try {
            getAllClaimsFromToken(jwt); // This will throw an exception if the token is invalid
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    // Method to extract the username (email) from the JWT token
    public String getUserNameFromJwtToken(String jwt) {
        return getEmailFromToken(jwt);
    }
}


