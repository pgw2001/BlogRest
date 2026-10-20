package com.kimyanu.blog.security;

import com.kimyanu.blog.model.Member;
import com.kimyanu.blog.repository.MemberRepository;
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
    private final MemberRepository memberRepository;

    private static UsernamePasswordAuthenticationToken getUsernamePasswordAuthenticationToken(Member member) {
        CustomUserDetails userDetails = new CustomUserDetails(member);

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

                // 4. 토큰의 회원 번호로 DB에서 회원을 찾는다.
                //    토큰은 발급된 뒤에 취소할 수 없어서, 탈퇴한 회원의 토큰도 만료될 때까지 서명 확인을 통과한다.
                //    요청마다 회원이 아직 있는지 확인해서 탈퇴한 회원의 토큰을 막는다.
                Long memberId = Long.valueOf(claims.getSubject());
                Member member = memberRepository.findByIdAndDeletedAtIsNull(memberId).orElse(null);

                if (member != null) {
                    // 5. 찾은 회원으로 인증된 Authentication 객체를 생성한다.
                    UsernamePasswordAuthenticationToken authentication = getUsernamePasswordAuthenticationToken(member);

                    // 6. 인증 정보를 스프링 시큐리티 컨텍스트에 저장한다.
                    SecurityContextHolder.getContext().setAuthentication(authentication);
                }

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
