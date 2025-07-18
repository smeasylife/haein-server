package ksm.haein.user.service;

import ksm.haein.user.dto.SignUpRequestForm;
import ksm.haein.user.entity.Member;
import ksm.haein.user.exception.MemberNotFoundException;
import ksm.haein.user.repository.CredentialRepository;
import ksm.haein.user.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class MemberService {
    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;
    private final CredentialRepository credentialRepository;


    public void localLogin(SignUpRequestForm signUpRequestForm) {
        Member member = saveMember(signUpRequestForm);
    }

    @Transactional
    public Member saveMember(SignUpRequestForm signUpRequestForm){
        validateDuplicatedUsername(signUpRequestForm.getEmail());

        Member member = memberRepository.save(Member.builder()
                .email(signUpRequestForm.getEmail())
                .password(passwordEncoder.encode(signUpRequestForm.getPassword()))
                .nickname(signUpRequestForm.getNickname())
                .phoneNumber(signUpRequestForm.getPhoneNumber())
                .build());

        memberRepository.save(member);
        return member;
    }

    private void validateDuplicatedUsername(String email){
        memberRepository.findByEmail(email)
                .ifPresent(m -> {
                    throw new MemberNotFoundException("Already existing username");
                });
    }

    private Member getUserIfNotExistsSignup(String email, String nickname) {
        return userRepository.findByEmail(email)
                .orElseGet(() -> userService.save(email, name));
    }
}
