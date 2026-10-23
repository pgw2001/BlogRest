package com.kimyanu.blog.security;

import com.kimyanu.blog.dto.auth.GoogleUserInfo;
import com.kimyanu.blog.enums.AuthProvider;
import com.kimyanu.blog.model.Member;
import com.kimyanu.blog.service.RefreshTokenService;
import com.kimyanu.blog.service.SocialAccountService;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.jspecify.annotations.NullMarked;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Objects;

@Component
@NullMarked
public class OAuth2SuccessHandler implements AuthenticationSuccessHandler {

    private final SocialAccountService socialAccountService;
    private final RefreshTokenService refreshTokenService;
    private final RefreshTokenCookie refreshTokenCookie;
    private final String frontendUrl;

    public OAuth2SuccessHandler(SocialAccountService socialAccountService,
                                RefreshTokenService refreshTokenService,
                                RefreshTokenCookie refreshTokenCookie,
                                @Value("${app.frontend-url}")String frontendUrl) {
        this.socialAccountService = socialAccountService;
        this.refreshTokenService = refreshTokenService;
        this.refreshTokenCookie = refreshTokenCookie;
        this.frontendUrl = frontendUrl;
    }

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request,
                                        HttpServletResponse response,
                                        Authentication authentication) throws IOException, ServletException {

        // 소셜 로그인의 인증 결과는 모두 OAuth2User 다
        // 구글처럼 OpenID Connect를 쓰는 곳은 OidUser(OAuth2User의 하위 타입)가 오고, 깃허브처럼 쓰지 않는 곳은 OAuth2User가 된다.
        // OidUser로 형변환하면 깃허브 같은 곳에서 ClassCastException이 안므로 공통 타입인 OAuth2User로 받는다.
        OAuth2User oAuth2User = (OAuth2User) authentication.getPrincipal();

        // OAuth2User는 사용자 정보를 Map(attribute)으로 갖고 있어서 이름(키)으로 꺼낸다.
        // 키 이름은 provider마다 다르다. 아래는 구글의 키 이름이다.
        GoogleUserInfo googleUserInfo = new GoogleUserInfo(
                Objects.requireNonNull(oAuth2User).getAttribute("sub"),
                oAuth2User.getAttribute("email"),
                oAuth2User.getAttribute("email_verified"),
                oAuth2User.getAttribute("name")
        );

        // 구글이 확인해 준 이메일이 실제로 그 사람의 것인지 확인되지 않았다면 로그인 실패로 처리한다.
        if(!Boolean.TRUE.equals(googleUserInfo.emailVerified())) {
            response.sendRedirect(frontendUrl + OAuth2FailureHandler.LOGIN_ERROR_PATH);
            return;
        }

        // 구글이 확인해준 값으로 우리 회원을 직접 찾거나 만듦
        Member member = socialAccountService.loginOrRegister(AuthProvider.GOOGLE, googleUserInfo);
        String refreshToken = refreshTokenService.issued(member.getId());
        response.addHeader(HttpHeaders.SET_COOKIE, refreshTokenCookie.create(refreshToken).toString());
        response.sendRedirect(frontendUrl);
    }
}
