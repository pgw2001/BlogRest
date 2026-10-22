package com.kimyanu.blog.dto.auth;

import com.kimyanu.blog.security.CustomUserDetails;

// 재발급의 결과. RefreshTokenService가 AuthController에 넘겨 주는 값이며, 응답으로 나가지 않는다.
// - userDetails: 토큰의 주인. 커늩롤러가 이것으로 새 액세스 토큰을 만든다.
// - refreshToken: 새로 발급한 리프레시 토큰. 컨트롤러가 쿠키에 담는다.
public record RefreshResult(CustomUserDetails userDetails, String refreshToken) {
}
