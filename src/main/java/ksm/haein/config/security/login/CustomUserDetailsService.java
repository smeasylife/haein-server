package ksm.haein.config.security.login;

import ksm.haein.user.dto.MemberLoginData;
import ksm.haein.user.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {
    private final MemberService memberService;


    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        MemberLoginData memberLoginData = memberService.getMemberLoginData(email);
        return new CustomUser(memberLoginData);
    }
}
