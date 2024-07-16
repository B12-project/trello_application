package b12.trello.global.security;

import b12.trello.domain.user.entity.User;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;

public class UserDetailsImpl implements UserDetails {
    private final b12.trello.domain.user.entity.User user;

    public UserDetailsImpl(b12.trello.domain.user.entity.User user) {
        this.user = user;
    }

    public b12.trello.domain.user.entity.User getUser() {
        return user;
    }

    // 무조건 상속 받아야 함
    // 권한 가져오는 부분
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        User.UserAuth role = user.getAuth();
        String authority = role.toString();

        SimpleGrantedAuthority simpleGrantedAuthority = new SimpleGrantedAuthority(authority);
        Collection<GrantedAuthority> authorities = new ArrayList<>();
        authorities.add(simpleGrantedAuthority);

        return authorities;
    }

    // password 가져오기
    @Override
    public String getPassword() {
        return user.getPassword();
    }

    // username 가져오기
    @Override
    public String getUsername() {
        return user.getEmail();
    }

    // 계정 만료 안됨
    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    //계정 잠겨있지 않음
    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    // 계정 활성화
    @Override
    public boolean isEnabled() {
        return true;
    }
}
