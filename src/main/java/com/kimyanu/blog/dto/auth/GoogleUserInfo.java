package com.kimyanu.blog.dto.auth;

// 구글 로그인이 성공했을 때 구글이 넘겨 주는 사용자 정보. OAuth2SuccessHandler가 만들어서 응답으로 보낸다.
// - sub: 구글이 그 계정에 붙여 둔 고유 번호. 이메일은 바뀔 수 있지만 이 값은 바뀌지 않아서 SocialAccount의 providerId로 저장한다.
// - email: 구글 계정의 이메일
// - emailVerified: 구글이 그 이메일의 주인임을 확인했는지 여부. 구글의 키 이름은 email_verified 다.
// - name: 구글 계정에 등록된 이름
// 구글이 값을 주지 않으면 null이 들어갈 수 있어서 emailVerified도 boolean
public record GoogleUserInfo(String sub, String email, Boolean emailVerified, String name) {
}
