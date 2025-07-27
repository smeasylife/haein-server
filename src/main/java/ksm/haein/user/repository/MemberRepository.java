package ksm.haein.user.repository;

import ksm.haein.user.dto.MemberLoginData;
import ksm.haein.user.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member, Long> {
    Optional<Member> findByEmail(String email);
    @Query("""
    SELECT new ksm.haein.user.dto.MemberLoginData(
        m.id,
        m.email,
        m.phoneNumber,
        m.role,
        m.createdAt,
        c.id,
        c.identityProvider,
        c.password
    ) 
    FROM Credential c
    JOIN c.member m
    WHERE m.email = :email
""")
    Optional<MemberLoginData> findMemberLoginDataByEmail(String email);
}
