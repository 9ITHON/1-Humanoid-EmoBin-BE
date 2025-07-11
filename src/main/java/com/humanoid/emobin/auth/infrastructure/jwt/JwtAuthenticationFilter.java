package com.humanoid.emobin.auth.infrastructure.jwt;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.humanoid.emobin.global.exception.*;
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
        if (token != null) {
            try {
                if (jwtProvider.validateToken(token)) {
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
                }
            } catch (CustomException ex) {
                ErrorCode errorCode = ex.getErrorCode();
                response.setStatus(errorCode.getStatus());
                response.setContentType("application/json;charset=UTF-8");
                ErrorResponse errorResponse = new ErrorResponse(errorCode.getCode(), errorCode.getMessage());
                String json = new ObjectMapper().writeValueAsString(errorResponse);
                response.getWriter().write(json);
                return;
            }
        }
        chain.doFilter(request, response);
    }
}