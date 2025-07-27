package ksm.haein.config.security.login;

import ksm.haein.user.dto.MemberLoginData;
import ksm.haein.user.entity.Credential;
import ksm.haein.user.entity.Member;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class CustomUser implements UserDetails {
    private final MemberLoginData memberLoginData;
    private final List<GrantedAuthority> authorities;

    public CustomUser(MemberLoginData memberLoginData) {
        this.memberLoginData = memberLoginData;
        this.authorities = List.of(
                new SimpleGrantedAuthority(memberLoginData.getRole().name())
        );
    }


    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    @Override
    public String getPassword() {
        return memberLoginData.getPassword();
    }

    @Override
    public String getUsername() {
        return memberLoginData.getEmail();
    }
}
