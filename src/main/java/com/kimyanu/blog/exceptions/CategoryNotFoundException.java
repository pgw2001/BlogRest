package com.kimyanu.blog.exceptions;

import lombok.Getter;

/**
 * 1. 예외의 종류
 *  1.1. 체크 예외
 *      1.1.1. RunTimeException을 상속받지 않은 예외
 *      1.1.2. 컴파일 시점에 예외처리를 강제하는 예외
 *      1.1.3. 예외처리를 하지 않으면 컴파일 에러가 발생한다.
 *      1.1.4. 에외처리 방법
 *      1.1.5. try-catch 문을 사용하여 예외를 처리한다.
 *      1.1.6. throws 키워드를 사용하여 예외를 호출한 메서드로 전달한다.
 *      1.1.7. 예외를 처리하지 않고 호출한 메서드로 전달하면 호출한 메서드에서도 예외처리를 해야 한다.
 *      1.1.8. IOException, SQLException, ClassNotFoundException 등이 체크 예외에 해당한다.
 *   1.2. 언체크 예외(런타임 예외)
 *      1.2.1. RuntimeException을 상속받은 예외
 *      1.2.2. 코드를 실행하는 도중에 발생하는 예외로, 컴파일 시점에 예외처리 여부를 검사하지 않는다.
 *      1.2.3. 예외처리를 하지 않아도 컴파일 에러가 발생하지 않는다.
 *      1.2.4. 예외처리 방법
 *      1.2.5. try-catch 문을 사용하여 예외를 처리한다.
 *      1.2.6. throws 키워드를 사용하여 예외를 호출한 메서드로 전달한다.
 *      1.2.7. 에외를 처리하지 않고 호출한 메서드로 전달하면 호출한 메서드에서도 예외처리를 해야 한다.
 *      1.2.8. IllegalStateException, NoSuchElementException, ClassCastException 등이 인체크 예외에 해당한다.
 */

@Getter
public class CategoryNotFoundException extends RuntimeException {
    private final Long categoryId;

    public CategoryNotFoundException(Long categoryId) {
        super(String.format("ID가 %s인 카테고리를 찾을 수 없습니다.", categoryId));
        this.categoryId = categoryId;
    }
}
