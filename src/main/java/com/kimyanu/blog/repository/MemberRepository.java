package com.kimyanu.blog.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.kimyanu.blog.model.Member;

@Repository
public interface MemberRepository extends JpaRepository<Member, Long> {
}
