package com.kimyanu.blog.config;

import com.kimyanu.blog.security.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.HeadersConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(
            HttpSecurity http,
            CustomAuthenticationEntryPoint authenticationEntryPoint,
            CustomAccessDeniedHandler accessDeniedHandler,
            JwtProvider jwtProvider) throws Exception {
        http
                // URL 기반의 권한 설정
                // .requestMatchers() : 특정 URL 패턴에 대한 권한 설정
                // anyRequest() : 나머지 모든 요청
                // permitAll() : 모든 사용자에게 허용
                // authenticated() : 인증된 사용자만 허용
                // /error
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/h2-console/**").permitAll()
                        .requestMatchers("/api/public/**").permitAll()
                        .requestMatchers("/error").permitAll()
                        .requestMatchers("/api/admin/**").hasRole("ADMIN")  // hasAuthority("ADMIN")도 사용가능한데 앞에 ROLE_ 지워야됨
                        .anyRequest().authenticated()
                )

                // CSRF 보호 기능을 활성화 (<form action="">) 요청에 대해 CSRF 토큰을 검증하도록 설정
                // 사용자 자신의 의지와 무관하게 공격자가 의도한 수정, 삭제, 등록 등(POST, PUT, DELETE)의 행위를 서버에 수행하게 만드는 공격을 의미
                // CSRF 공격을 방지하기 위해 서버는 요청에 CSRF 토큰을 포함하도록 요구
                // 그래서 아래 설정은 CSRF 보호 기능을 비활성화하는 설정으로, REST API에서는 일반적으로 CSRF 보호를 비활성화하는 경우가 많다.
                .csrf(AbstractHttpConfigurer::disable)

                // H2 콘솔을 위한 설정
                // 같은 출처에서만 프레임을 허용하도록 설정
                // 이게 없이면 H2 콘솔 화면이 깨진다. (X-Frame-Options DENY 에러)
                .headers(headers -> headers
                        .frameOptions(HeadersConfigurer.FrameOptionsConfig::sameOrigin)
                )

                // 세션을 만들지 않고 사용하지도 않는다
                // 이게 없으면 인증 없이 인증이 필요한 경로를 호출활 때 세선이 만들어지고, 401 응답에 JSESSIONID 쿠키가 내려온다.
                .sessionManagement(session -> session
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                )

                .exceptionHandling(exception -> exception
                        .authenticationEntryPoint(authenticationEntryPoint)
                        .accessDeniedHandler(accessDeniedHandler)
                )
                // .addFilterBefore(filter, targetFilterClass) : 특정 필터를 다른 필터 앞에 추가
                .addFilterBefore(new JwtAuthenticationFilter(jwtProvider), UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(
            CustomUserDetailsService customUserDetailsService,
            PasswordEncoder passwordEncoder
    ) {
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider(customUserDetailsService);
        provider.setPasswordEncoder(passwordEncoder);
        return new ProviderManager(provider);
    }
}