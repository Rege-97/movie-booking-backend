package com.cinema.moviebooking.repository.member;

import com.cinema.moviebooking.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member, Long> {

    boolean existsByEmailAndDeletedAtIsNull(String email);

    Optional<Member> findByEmail(String email);

    boolean existsByEmail(String email);
}
