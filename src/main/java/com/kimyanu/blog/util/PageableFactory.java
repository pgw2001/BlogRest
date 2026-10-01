package com.kimyanu.blog.util;

import org.jspecify.annotations.NonNull;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.util.List;


public final class PageableFactory {
    private PageableFactory() {

    }

    public static @NonNull Pageable getPageable(Integer pageNumber,
                                                Integer pageSize,
                                                String sortBy,
                                                String sortOrder,
                                                List<String> sortableFields) {
        if(pageNumber < 1 || pageSize < 1) {
            throw new IllegalArgumentException("pageNumber와 pageSize는 1 이상이어야 합니다.");
        }

        if (!sortableFields.contains(sortBy)) {
            throw new IllegalArgumentException("정렬할 수 없는 필드입니다. : "  + sortBy);
        }

        if (!sortOrder.equalsIgnoreCase("asc") && !sortOrder.equalsIgnoreCase("desc")) {
            throw new IllegalArgumentException("sortOrder는 asc 또는 desc만 가능합니다. :" + sortOrder);
        }

        // Sort 객체
        // 스프링 데이터 JPA에서 제공하는 객체로, 정렬 기준과 방향을 설정할 수 있다.
        Sort sort = Sort.by(
                sortOrder.equalsIgnoreCase("asc")
                        ? Sort.Direction.ASC
                        : Sort.Direction.DESC,
                sortBy
        );

        return PageRequest.of(pageNumber - 1, pageSize, sort);
    }
}
