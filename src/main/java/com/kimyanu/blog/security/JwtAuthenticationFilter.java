package com.kimyanu.blog.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

// 요청에 담긴 토큰을 확인해서, 이 요청을 누가 보냈는지 스프링 시큐리티에 알려주는 필터
// Basic 인증에서는 시큐리티가 기본으로 해주던 일을 토큰 방식에 맞게 직접 작성한 것이다.
// OncePerRequestFilter를 상속받으면, 요청이 들어올 때마다 doFilterInternal() 메서드가 실행된다.
@RequiredArgsConstructor
@NullMarked
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtProvider jwtProvider;

    private static UsernamePasswordAuthenticationToken getUsernamePasswordAuthenticationToken(Long memberId, String email, String role) {
        CustomUserDetails userDetails = new CustomUserDetails(memberId, email, role);

        // 6. 인증된 사용자라는 Authentication 객체를 생성한다.
        // - 첫 번째 인자: 인증된 사용자 정보(CustomUserDetails)
        // - 두 번째 인자: 비밀번호(null, 토큰으로 인증했으므로 필요 없음)
        // - 세 번째 인자: 권한 목록(ROLE_USER, ROLE_ADMIN 등)
        return new UsernamePasswordAuthenticationToken(
                userDetails,
                null,
                userDetails.getAuthorities()
        );
    }

    private static @Nullable String resolveToken(HttpServletRequest request) {
        String header = request.getHeader("Authorization");
        String token = null;
        if (header != null && header.startsWith("Bearer ")) {
            token = header.substring("Bearer ".length());
        }
        return token;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {

        // 1. 요청의 헤더에서 토큰을 꺼낸다. 토큰이 없으면 null이다.
        String token = resolveToken(request);

        // 2. 토큰이 존재하면 검증하고, 인증 정보를 스프링 시큐리티 컨텍스트에 저장한다.
        if (token != null) {
            try {
                // 3. 토큰을 검증하고 내용을 꺼낸다.
                Claims claims = jwtProvider.parseToken(token);

                // 4. 토큰의 subject와 claims에서 필요한 정보를 꺼낸다.
                Long memberId = Long.valueOf(claims.getSubject());
                String email = claims.get("email", String.class);
                String role = claims.get("role", String.class);

                // 5. 토큰에 들어있는 정보로 스프링 시큐리티가 인증 관리에서 사용할 CustomUserDetails 객체를 생성한다.
                // (DB를 조회하지 않고, 토큰에 들어있는 정보만으로 인증 정보를 만든다.)
                UsernamePasswordAuthenticationToken authentication = getUsernamePasswordAuthenticationToken(memberId, email, role);

                // 7. 스프링 시큐리티 컨텍스트(전역 저장소)에 인증 정보를 저장한다.
                // 여기에 인증 객체가 등록되어져 있어야 "이 요청은 로그인된 사용자의 요청이구나"라고 판단할 수 있다.
                SecurityContextHolder.getContext().setAuthentication(authentication);
            } catch (JwtException | IllegalArgumentException e) {
                // 8. 토큰이 유효하지 않으면 인증 정보를 지운다.
                // 이후의 필터에서 인증되지 않은 상태로 처리된다.
                SecurityContextHolder.clearContext();
            }
        }

        // 9. 다음 필터로 요청을 넘긴다.
        filterChain.doFilter(request, response);
    }
}
