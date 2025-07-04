package com.humanoid.emobin.infrastructure.jwt;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;

@Component
public class JwtProvider {

    private final Key key;

    public JwtProvider(@Value("${jwt.secret}") String secret) {
        this.key = Keys.hmacShaKeyFor(secret.getBytes());
    }

    public String createAccessToken(Long memberId) {
        long ACCESS_TOKEN_EXPIRE_TIME = 1000L * 60 * 30; //30분
        return createToken(memberId, "access", ACCESS_TOKEN_EXPIRE_TIME);
    }

    public String createRefreshToken(Long memberId) {
        long REFRESH_TOKEN_EXPIRE_TIME = 1000L * 60 * 60 * 24 * 14; //14일
        return createToken(memberId, "refresh", REFRESH_TOKEN_EXPIRE_TIME);
    }

    private String createToken(Long memberId, String type, long expireTime) {

        return Jwts.builder()
                .setSubject(String.valueOf(memberId))
                //.claim("type", type)                      // access or refresh
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + expireTime))
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();
    }

    public String getMemberIdFromToken(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody()
                .getSubject();
    }

    public boolean validateToken(String token) {
        try {
            Jwts.parserBuilder()
                    .setSigningKey(key)
                    .build()
                    .parseClaimsJws(token);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public String resolveToken(HttpServletRequest request) {
        String bearer = request.getHeader("Authorization");
        return (bearer != null && bearer.startsWith("Bearer ")) ? bearer.substring(7) : null;
    }
}
