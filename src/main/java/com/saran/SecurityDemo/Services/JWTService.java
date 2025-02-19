package com.saran.SecurityDemo.Services;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import java.security.Key;

import java.security.NoSuchAlgorithmException;
import java.util.Base64;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;


@Service
public class JWTService {

    private String secretKey  =  "";

    //  generating Secret Key for signature
    public JWTService() {
        try {
            KeyGenerator keyGen = KeyGenerator.getInstance("HmacSHA256");
            SecretKey key = keyGen.generateKey();
            secretKey = Base64.getEncoder().encodeToString(key.getEncoded());
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }

    }

    public String generateToken(String username) {
        Map<String, Object> claims = new HashMap<>();

        // JWT Token = Header + Payload + Sign
        // Header -> algorithm used and token type
        // Payload -> subject + IssuedAt + expiration
        // sign -> Signature for Verification

        return Jwts.builder()
                .claims()
                .add(claims) // claims -> details of the payload // adding claims to the map
                .subject(username)
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date(System.currentTimeMillis() + 60 *60 *30))
                .and()
                .signWith(getSecretKey())
                .compact();
    }

    public SecretKey getSecretKey() {
        byte[] keyBytes = Decoders.BASE64.decode(secretKey); // hmac alg accepts in byte type
        return Keys.hmacShaKeyFor(keyBytes); // generating key
    }

    public String extractUserName(String token) {
        return extractClaim(token,Claims::getSubject);
    }

    private <T>  T extractClaim(String token, Function<Claims,T> claimsResolver) {
        Claims claims = extarctAllClaims(token);
        return claimsResolver.apply(claims);
    }

    private Claims extarctAllClaims(String token) {
        return Jwts.parser()
                .verifyWith(getSecretKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    public boolean validateToken(String token, UserDetails userDetails) {
        String username = extractUserName(token);
        return (userDetails.getUsername().equals(username) && !isTokenExpired(token));
    }

    private boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date() );
    }

    private Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }


}
