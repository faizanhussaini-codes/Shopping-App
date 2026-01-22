package com.ShoppingCart.ShoppingCart.security.user;

import com.ShoppingCart.ShoppingCart.entity.User;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.jspecify.annotations.Nullable;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ShopUserDetails implements UserDetails {

    private Long id;
    private String email;
    private String password;
    private Collection<GrantedAuthority> authorities;

    //This method converts my User entity into UserDetails by mapping roles into GrantedAuthority so Spring Security can authenticate and authorize the user.
    //Spring Security needs roles as GrantedAuthority & You mapped roles into GrantedAuthority for Spring Security But Other parts of your application need roles as String
    public static ShopUserDetails buildUserDetails(User user){
        List<GrantedAuthority> authorities = user.getRoles()//Here we are getting roles and mapping it with the user
                .stream()
                .map(role -> new SimpleGrantedAuthority(role.getName()))
                .collect(Collectors.toList());
        return new ShopUserDetails( // Creating a new object of Shopuserdetails as spring understand that only
                user.getId(),
                user.getEmail(),
                user.getPassword(),
                authorities);
    }

    //These methods expose user credentials, roles, and account status to Spring Security so it can authenticate and authorize the user correctly.
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    @Override
    public @Nullable String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return email;
    }

    @Override
    public boolean isAccountNonExpired() {
        return UserDetails.super.isAccountNonExpired();
    }

    @Override
    public boolean isAccountNonLocked() {
        return UserDetails.super.isAccountNonLocked();
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return UserDetails.super.isCredentialsNonExpired();
    }

    @Override
    public boolean isEnabled() {
        return UserDetails.super.isEnabled();
    }
}
