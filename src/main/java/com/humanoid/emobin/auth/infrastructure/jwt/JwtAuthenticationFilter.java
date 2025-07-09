package com.humanoid.emobin.auth.infrastructure.jwt;

import com.humanoid.emobin.global.exception.CustomException;
import com.humanoid.emobin.global.exception.AuthErrorCode;
import com.humanoid.emobin.auth.infrastructure.redis.RedisService;
import io.jsonwebtoken.Claims;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtProvider jwtProvider;
    private final RedisService redisService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
            throws ServletException, IOException {

        String path = request.getRequestURI();
        if (path.startsWith("/api/test") || path.startsWith("/auth")) {
            chain.doFilter(request, response);
            return;
        }

        String token = jwtProvider.resolveToken(request);

        if (token != null && jwtProvider.validateToken(token)) {
            Claims claims = jwtProvider.parseClaims(token);
            String tokenId = claims.getId();

            // 블랙리스트 검사
            if (redisService.exists("blacklist:access:" + tokenId)) {
                throw new CustomException(AuthErrorCode.TOKEN_BLACKLISTED);
            }

            Long memberId = claims.get("memberId", Long.class);

            UsernamePasswordAuthenticationToken authentication =
                    new UsernamePasswordAuthenticationToken(memberId, null, null);

            SecurityContextHolder.getContext().setAuthentication(authentication);
            chain.doFilter(request, response);
        }else{
            throw new CustomException(AuthErrorCode.TOKEN_NOT_FOUND);
        }
    }
}