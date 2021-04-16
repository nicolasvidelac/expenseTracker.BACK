package com.group.gastos.others.jwt;

import com.group.gastos.others.enums.KeysEnum;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Service
public class JwtToken {
    private final String secret = KeysEnum.encoding_key.toString();

    public String getUsername(String token){
        return getClaim(token, Claims::getSubject);
    }

    private Date getExpirationDate(String token){
        return getClaim(token, Claims::getExpiration);
    }

    private <T> T getClaim(String token, Function<Claims, T> claimsResolver){
        final Claims claims = getAllClaims(token);
        return claimsResolver.apply(claims);
    }

    private Claims getAllClaims(String token){
        return Jwts.parserBuilder()
                .setSigningKey(secret.getBytes(StandardCharsets.UTF_8))
                .build().parseClaimsJws(token).getBody();
    }

    private Boolean isTokenExpired(String token){
        return getExpirationDate(token).before(new Date());
    }

    public String generateToken(String username){
        Map<String, Object> claims = new HashMap<>();
        return createToken(claims, username);
    }

    private String createToken(Map<String, Object> claims, String subject){
        return Jwts.builder()
                .setClaims(claims)
                .setSubject(subject)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60 * 24 * 14))
                .signWith(
                        Keys.hmacShaKeyFor(
                                secret.getBytes(StandardCharsets.UTF_8)),
                                SignatureAlgorithm.HS512
                        )
                .compact();
    }

    public Boolean isTokenValid(String token, UserDetails userDetails){
        final String username = getUsername(token);
        return (username.equals(userDetails.getUsername()) && !isTokenExpired(token));
    }
}
