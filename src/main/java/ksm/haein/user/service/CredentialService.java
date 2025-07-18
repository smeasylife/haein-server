package ksm.haein.user.service;

import ksm.haein.user.repository.CredentialRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class CredentialService {
    private final CredentialRepository credentialRepository;
}
