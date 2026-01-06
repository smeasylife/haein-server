package ksm.haein.user.dto;

import java.time.LocalDateTime;

public record MemberData(
        Long id,
        String nickname,
        String email,
        String phoneNumber,
        LocalDateTime createdAt,
        String role
) {
}
