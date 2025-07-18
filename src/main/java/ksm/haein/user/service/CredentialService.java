package ksm.haein.user.service;

import ksm.haein.user.dto.SignUpRequestForm;
import ksm.haein.user.entity.Credential;
import ksm.haein.user.entity.Member;
import ksm.haein.user.enums.IdentityProvider;
import ksm.haein.user.repository.CredentialRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class CredentialService {
    private final CredentialRepository credentialRepository;
    private final PasswordEncoder passwordEncoder;

    public Credential saveLocalCredential(SignUpRequestForm signUpRequestForm, Member member) {
        Credential credential = Credential.builder()
                .member(member)
                .password(passwordEncoder.encode(signUpRequestForm.getPassword()))
                .identityProvider(IdentityProvider.LOCAL)
                .build();

        return credentialRepository.save(credential);
    }
}
