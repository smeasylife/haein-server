package ksm.haein.user;

import ksm.haein.user.dto.SignUpRequestForm;
import ksm.haein.user.entity.Member;
import ksm.haein.user.enums.Role;
import ksm.haein.user.exception.MemberAlreadyExistsException;
import ksm.haein.user.repository.MemberRepository;
import ksm.haein.user.service.CredentialService;
import ksm.haein.user.service.MemberService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("회원 서비스 테스트")
class MemberServiceTest {

    @Mock
    private MemberRepository memberRepository;

    @Mock
    private CredentialService credentialService;

    @InjectMocks
    private MemberService memberService;

    @Test
    @DisplayName("로컬 회원가입 - 성공")
    void testLocalSignupSuccess() {
        // Given
        SignUpRequestForm form = createSignUpForm("test@example.com", "testuser", "password123!", "01012345678");

        when(memberRepository.findByEmail(anyString())).thenReturn(Optional.empty());
        when(memberRepository.save(any(Member.class))).thenAnswer(invocation -> invocation.getArgument(0));

        // When
        memberService.localSignup(form);

        // Then
        verify(memberRepository).findByEmail(form.getEmail());
        verify(memberRepository, atLeastOnce()).save(any(Member.class));
        verify(credentialService).saveLocalCredential(form, any(Member.class));
    }

    @Test
    @DisplayName("로컬 회원가입 - 실패 (이미 존재하는 이메일)")
    void testLocalSignupFailureEmailExists() {
        // Given
        SignUpRequestForm form = createSignUpForm("existing@example.com", "existing", "password123!", "01012345678");

        Member existingMember = Member.builder()
                .id(1L)
                .email("existing@example.com")
                .role(Role.ROLE_USER)
                .build();

        when(memberRepository.findByEmail(form.getEmail())).thenReturn(Optional.of(existingMember));

        // When & Then
        assertThrows(MemberAlreadyExistsException.class, () -> memberService.saveMember(form));
        verify(memberRepository).findByEmail(form.getEmail());
        verify(memberRepository, never()).save(any(Member.class));
    }

    @Test
    @DisplayName("회원 저장 - 성공")
    void testSaveMemberSuccess() {
        // Given
        SignUpRequestForm form = createSignUpForm("new@example.com", "newuser", "password123!", "01012345678");

        when(memberRepository.findByEmail(anyString())).thenReturn(Optional.empty());
        when(memberRepository.save(any(Member.class))).thenAnswer(invocation -> {
            Member member = invocation.getArgument(0);
            return Member.builder()
                    .id(1L)
                    .email(member.getEmail())
                    .nickname(member.getNickname())
                    .phoneNumber(member.getPhoneNumber())
                    .build();
        });

        // When
        Member savedMember = memberService.saveMember(form);

        // Then
        assertNotNull(savedMember);
        assertEquals(form.getEmail(), savedMember.getEmail());
        assertEquals(form.getNickname(), savedMember.getNickname());
        assertEquals(form.getPhoneNumber(), savedMember.getPhoneNumber());
        verify(memberRepository).save(any(Member.class));
    }

    @Test
    @DisplayName("카카오 회원가입 - 신규 회원")
    void testSaveKakaoMemberNew() {
        // Given
        String email = "kakao@example.com";
        String nickname = "kakaouser";

        when(memberRepository.findByEmail(email)).thenReturn(Optional.empty());
        when(memberRepository.save(any(Member.class))).thenAnswer(invocation -> invocation.getArgument(0));

        // When
        Member member = memberService.getUserIfNotExistsSignup(email, nickname);

        // Then
        assertNotNull(member);
        assertEquals(email, member.getEmail());
        verify(memberRepository).findByEmail(email);
        verify(memberRepository, atLeastOnce()).save(any(Member.class));
        verify(credentialService).saveKakaoCredential(any(Member.class));
    }

    @Test
    @DisplayName("카카오 로그인 - 기존 회원")
    void testGetUserIfNotExistsSignupExistingMember() {
        // Given
        String email = "existing@example.com";
        String nickname = "existinguser";

        Member existingMember = Member.builder()
                .id(1L)
                .email(email)
                .nickname(nickname)
                .role(Role.ROLE_USER)
                .build();

        when(memberRepository.findByEmail(email)).thenReturn(Optional.of(existingMember));

        // When
        Member result = memberService.getUserIfNotExistsSignup(email, nickname);

        // Then
        assertNotNull(result);
        assertEquals(existingMember.getId(), result.getId());
        assertEquals(email, result.getEmail());
        verify(memberRepository).findByEmail(email);
        verify(memberRepository, never()).save(any(Member.class));
        verify(credentialService, never()).saveKakaoCredential(any(Member.class));
    }

    @Test
    @DisplayName("회원 로그인 데이터 조회 - 성공")
    void testGetMemberLoginDataSuccess() {
        // Given
        String email = "test@example.com";

        ksm.haein.user.dto.MemberLoginData loginData = new ksm.haein.user.dto.MemberLoginData(
                1L,
                email,
                "01012345678",
                Role.ROLE_USER,
                java.time.LocalDateTime.now(),
                null,
                ksm.haein.user.enums.IdentityProvider.LOCAL,
                "encodedPassword"
        );

        when(memberRepository.findMemberLoginDataByEmail(email)).thenReturn(Optional.of(loginData));

        // When
        ksm.haein.user.dto.MemberLoginData result = memberService.getMemberLoginData(email);

        // Then
        assertNotNull(result);
        assertEquals(email, result.getEmail());
        verify(memberRepository).findMemberLoginDataByEmail(email);
    }

    @Test
    @DisplayName("회원 로그인 데이터 조회 - 실패 (존재하지 않는 회원)")
    void testGetMemberLoginDataFailureMemberNotFound() {
        // Given
        String email = "nonexistent@example.com";

        when(memberRepository.findMemberLoginDataByEmail(email)).thenReturn(Optional.empty());

        // When & Then
        assertThrows(MemberAlreadyExistsException.class, () -> memberService.getMemberLoginData(email));
        verify(memberRepository).findMemberLoginDataByEmail(email);
    }

    private SignUpRequestForm createSignUpForm(String email, String nickname, String password, String phoneNumber) {
        SignUpRequestForm form = new SignUpRequestForm();
        form.setEmail(email);
        form.setNickname(nickname);
        form.setPassword(password);
        form.setPhoneNumber(phoneNumber);
        return form;
    }
}
