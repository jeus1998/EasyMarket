package easy.market.security;

import easy.market.entity.User;
import jakarta.annotation.Nullable;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import java.util.ArrayList;
import java.util.Collection;

public class CustomUserDetails implements UserDetails {
    private final String username;
    @Nullable
    private final String password;
    private final String role;
    public CustomUserDetails(String username, String role) {
        this.username = username;
        this.role = role;
        this.password = null;
    }
    public CustomUserDetails(User findUser) {
        this.username = findUser.getUsername();
        this.password = findUser.getPassword();
        this.role = findUser.getRole();
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }
    @Override
    public boolean isAccountNonLocked() {
        return true;
    }
    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }
    @Override
    public boolean isEnabled() {
        return true;
    }
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        Collection<SimpleGrantedAuthority> authorities = new ArrayList<>();
        authorities.add(new SimpleGrantedAuthority(this.role));
        return authorities;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return username;
    }

    public String getRole() {
        return role;
    }
}
