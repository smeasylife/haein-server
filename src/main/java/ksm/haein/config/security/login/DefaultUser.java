package ksm.haein.config.security.login;

import ksm.haein.user.entity.Credential;
import ksm.haein.user.entity.Member;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class DefaultUser implements UserDetails {
    private final Member member;
    private final Credential credential;
    private final List<GrantedAuthority> authorities;

    public DefaultUser(Member member) {
        this.member = member;
        this.authorities = new ArrayList<>();
    }


    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of();
    }

    @Override
    public String getPassword() {
        return "";
    }

    @Override
    public String getUsername() {
        return "";
    }
}
