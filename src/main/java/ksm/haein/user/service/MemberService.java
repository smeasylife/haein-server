package ksm.haein.user.service;

import ksm.haein.user.dto.MemberLoginData;
import ksm.haein.user.dto.SignUpRequestForm;
import ksm.haein.user.entity.Member;
import ksm.haein.user.exception.MemberAlreadyExistsException;
import ksm.haein.user.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class MemberService {
    private final MemberRepository memberRepository;
    private final CredentialService credentialService;

    @Transactional
    public void localSignup(SignUpRequestForm signUpRequestForm) {
        Member member = saveMember(signUpRequestForm);
        credentialService.saveLocalCredential(signUpRequestForm, member);
    }

    @Transactional
    public Member saveMember(SignUpRequestForm signUpRequestForm){
        validateDuplicatedUsername(signUpRequestForm.getEmail());

        Member member = memberRepository.save(Member.builder()
                .email(signUpRequestForm.getEmail())
                .nickname(signUpRequestForm.getNickname())
                .phoneNumber(signUpRequestForm.getPhoneNumber())
                .build());

        memberRepository.save(member);
        return member;
    }

    private void validateDuplicatedUsername(String email){
        memberRepository.findByEmail(email)
                .ifPresent(m -> {
                    throw new MemberAlreadyExistsException("Already existing username");
                });
    }

    private Member saveKakaoMember(String email, String nickname) {
        validateDuplicatedUsername(email);

        Member member = memberRepository.save(Member.builder()
                .email(email)
                .nickname(nickname)
                .build());

        credentialService.saveKakaoCredential(member);
        return memberRepository.save(member);
    }

    public Member getUserIfNotExistsSignup(String email, String nickname) {
        return memberRepository.findByEmail(email)
                .orElseGet(() -> saveKakaoMember(email, nickname));
    }

    public MemberLoginData getMemberLoginData(String email) {
        return memberRepository.findMemberLoginDataByEmail(email).orElseThrow(MemberAlreadyExistsException::new);
    }
}
