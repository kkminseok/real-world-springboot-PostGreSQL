package com.io.realworld.domain.aggregate.service;

import com.io.realworld.security.jwt.JwtConfig;
import com.io.realworld.domain.aggregate.user.entity.User;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Date;

@Service
@Getter
@AllArgsConstructor
public class JwtService {

    private final JwtConfig jwtConfig;

    private Key getSignKey(String secretKey){
        byte[] keyBytes = secretKey.getBytes(StandardCharsets.UTF_8);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    public Claims extractAllClaims(String token) throws ExpiredJwtException{
        return Jwts.parserBuilder()
                .setSigningKey(getSignKey(jwtConfig.getKey()))
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    public String getEmail(String token){
        return extractAllClaims(token).get("email",String.class);
    }

    public Boolean isTokenExpired(String token){
        final Date expiration = extractAllClaims(token).getExpiration();
        return expiration.before(new Date());
    }

    public String createToken(String email){
        Claims claims = Jwts.claims();
        claims.put("email",email);

        return Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + jwtConfig.getExpiry() * 1000))
                .signWith(getSignKey(jwtConfig.getKey()))
                .compact();
    }

    public Boolean validateToken(String token, User user){
        final String email = getEmail(token);
        return email.equals(user.getEmail()) && !isTokenExpired(token);
    }

}