package ksm.haein.user.dto;

import ksm.haein.user.enums.IdentityProvider;
import ksm.haein.user.enums.Role;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;

@AllArgsConstructor
@Getter
public class MemberLoginData {
    private Long memberId;

    private String email;

    private String phoneNumber;

    private Role role;

    private LocalDateTime createdAt;

    private Long credentialId;

    private IdentityProvider identityProvider;

    private String password;
}
