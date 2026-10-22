package com.kimyanu.blog.exceptions;

import com.kimyanu.blog.exceptions.custom.*;
import com.kimyanu.blog.security.RefreshTokenCookie;
import lombok.RequiredArgsConstructor;
import org.hibernate.LazyInitializationException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.FieldError;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.resource.NoResourceFoundException;

import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
@RequiredArgsConstructor
public class GlobalExecutionHandler {

    private final RefreshTokenCookie refreshTokenCookie;

    // [커스텀] 404 카테고리 없음
    // 없는 카테고리를 수정 및 삭제하거나, 게시글을 등록 및 수정할 떄 없는 CategoryId를 보내면 던진다.
    @ExceptionHandler(CategoryNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleCategoryNotFoundException(CategoryNotFoundException e) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ErrorResponse(e.getMessage()));
    }

    // [커스텀] 409 카테고리 이름 중복
    // 카테고리를 등록하거나 이름을 바꿀 때 같은 이름이 이미 있으면 던진다.
    @ExceptionHandler(DuplicateCategoryException.class)
    public ResponseEntity<ErrorResponse> handleDuplicateCategoryException(DuplicateCategoryException e) {
        return ResponseEntity.status(HttpStatus.CONFLICT).body(new ErrorResponse(e.getMessage()));
    }

    // [커스텀] 404 멤버 없음
    // 없는 회원이거나 탈퇴한 회원을 조회, 수정, 탈퇴, 권한 변경하려 하면 던진다.
    @ExceptionHandler(MemberNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleMemberNotFoundException(MemberNotFoundException e) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ErrorResponse(e.getMessage()));
    }

    // [커스텀] 409 이메일 혹은 닉네임 중복
    // 가입할 때 이메일이나 닉네임이 회원 정보를 수정할 때 닉네임이 이미 있으면 던진다.
    @ExceptionHandler(DuplicateMemberException.class)
    public ResponseEntity<ErrorResponse> handleDuplicateMemberException(DuplicateMemberException e) {
        return ResponseEntity.status(HttpStatus.CONFLICT).body(new ErrorResponse(e.getMessage()));
    }

    // [커스텀] 400 쓸 수 없는 이메일, 닉네임
    // 탈퇴한 회원에게 붙이는 형태(이메일 @deleted.local, 닉네임 탈퇴한회원_)로 가입하거나 닉네임을 바꾸려고 하면 던진다.
    @ExceptionHandler(ReservedMemberValueException.class)
    public ResponseEntity<ErrorResponse> handleInvalidRegisterException(ReservedMemberValueException e) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ErrorResponse(e.getMessage()));
    }

    // [커스텀] 404 게시글 없음
    // 없는 게시글을 조회, 수정, 삭제하려 하면 던진다.
    @ExceptionHandler(PostNotFoundException.class)
    public ResponseEntity<ErrorResponse> handlePostNotFoundException(PostNotFoundException e) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ErrorResponse(e.getMessage()));
    }

    // [커스텀] 404 댓글 없음
    // 없는 댓글을 조회, 수정, 삭제하려 하면 던진다.
    @ExceptionHandler(CommentNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleCommentNotFoundException(CommentNotFoundException e) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ErrorResponse(e.getMessage()));
    }

    // [커스텀] 409 좋아요 중복
    // 이미 좋아요를 누른 게시글에 다시 좋아요를 누르려고 하면 던진다.
    @ExceptionHandler(DuplicateLikesException.class)
    public ResponseEntity<ErrorResponse> handleDuplicateLikesException(DuplicateLikesException e) {
        return ResponseEntity.status(HttpStatus.CONFLICT).body(new ErrorResponse(e.getMessage()));
    }

    // [커스텀] 404 좋아요 없음
    // 좋아요를 누르지 않은 게시글에 좋아요를 취소하려 하면 던진다.
    @ExceptionHandler(LikesNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleLikesNotFoundException(LikesNotFoundException e) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ErrorResponse(e.getMessage()));
    }

    // [커스텀] 409 관리자 역할 변경 방지
    // 관리자가 자신의 역할을 변경하려고 할 때 던지는 에러
    @ExceptionHandler(RoleException.class)
    public ResponseEntity<ErrorResponse> handleRoleException(RoleException e) {
        return ResponseEntity.status(HttpStatus.CONFLICT).body(new ErrorResponse(e.getMessage()));
    }

    // [커스텀] 403 접근 권한 없음
    // 비즈니스 로직에서 권한이 없는 요청을 하면 던진다. (본인만 수정/삭제 가능한 API)
    @ExceptionHandler(ForbiddenException.class)
    public ResponseEntity<ErrorResponse> handleForbiddenException(ForbiddenException e) {
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(new ErrorResponse(e.getMessage()));
    }

    // [커스텀] 401 잘못된 리프레시 토큰
    // 리프레시 토큰이 없으면 던진다. (쿠키에 refreshToken이 없거나, 서버에 저장된 리프레시 토큰과 다르거나, 만료되었거나)
    @ExceptionHandler(InvalidRefreshTokenException.class)
    public ResponseEntity<ErrorResponse> handleInvalidRefreshTokenException(InvalidRefreshTokenException e) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                .header(HttpHeaders.SET_COOKIE, refreshTokenCookie.expire().toString())
                .body(new ErrorResponse(e.getMessage()));
    }

    // [스프링] 409 DB 제약 조건 위반
    // 지정하거나 지울 때 DB의 제약(유니크, 외래 키)에 걸리면 스프링(JPA)이 던진다.
    // - 같은 요청이 동시에 와서 서비스의 중복 검사를 둘 다 통과한 경우, DB에 저장할 때 유니크 제약 조건에 걸리면 스프링이 던진다.
    // - 굳이 있는 카테고리를 지우려는 경우, 굳이 카테고리를 참조하고 있어서 외래 키 제약 조건에 걸리면 스프링이 던진다.

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<ErrorResponse> handleDataIntegrityViolationException() {
        return ResponseEntity.status(HttpStatus.CONFLICT).body(new ErrorResponse("데이터 무결성 제약 조건 위반입니다."));
    }
    // [스프링] 400 요청 본문을 읽을 수 없음
    // 본문이 없거나, JSON 형식이 잘못되었거나, DTO의 필드 타입과 맞지 않으면 스프링이 던진다.

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ErrorResponse> handleHttpMessageNotReadableException() {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ErrorResponse("요청 본문을 읽을 수 없습니다. JSON 형식을 확인해주세요"));
    }
    // [스프링] 400 요청 본문의 검증 실패
    // @Valid를 붙인 요청 본문(@RequestBody)의 검증에 실패하면 스프링이 던진다.

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleMethodArgumentNotValidException(MethodArgumentNotValidException e) {

        Map<String, String> errors = new HashMap<>();
        for(FieldError error : e.getBindingResult().getFieldErrors()) {
            errors.put(error.getField(), error.getDefaultMessage());
        }

        ErrorResponse response = new ErrorResponse("입력값이 올바르지 않습니다." + errors);

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    }
    // [스프링] 400 경로, 쿼리 값의 타입 불일치
    // 경로 변수나 쿼리 파라미터를 타입으로 바꿀 수 없으면 스프링이 던진다.

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<ErrorResponse> handleMethodArgumentTypeMismatchException() {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ErrorResponse("경로 또는 쿼리 값의 타입이 올바르지 않습니다."));
    }
    // [스프링] 405 지원하지 않는 HTTP 메서드
    // 주소는 있지만, 그 메서드를 받는 API가 없으면 스프링이 던진다.

    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public ResponseEntity<ErrorResponse> handleHttpRequestMethodNotSupportedException(HttpRequestMethodNotSupportedException e) {
        return ResponseEntity.status(HttpStatus.METHOD_NOT_ALLOWED).headers(e.getHeaders()).body(new ErrorResponse("지원하지 않는 HTTP 메서드입니다."));
    }

    // [스프링] 404 요청한 경로를 찾을 수 없음
    // 존재하지 않는 경로로 요청하면 스프링이 던진다.
    @ExceptionHandler(NoResourceFoundException.class)
    public ResponseEntity<ErrorResponse> handleNoResourceFoundException() {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ErrorResponse("요청한 경로를 찾을 수 없습니다."));
    }

    // [스프링] 500 영속성 컨텍스트 닫힘
    @ExceptionHandler(LazyInitializationException.class)
    public ResponseEntity<ErrorResponse> handleLazyInitializationException() {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ErrorResponse("영속성 컨텍스트가 닫혀서 LAZY 연관을 읽을 수 없습니다. OSIV 설정을 확인하세요."));
    }


}


