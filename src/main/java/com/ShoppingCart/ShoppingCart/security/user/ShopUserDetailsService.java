package com.ShoppingCart.ShoppingCart.security.user;

import com.ShoppingCart.ShoppingCart.entity.User;
import com.ShoppingCart.ShoppingCart.repo.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ShopUserDetailsService implements org.springframework.security.core.userdetails.UserDetailsService {

    private final UserRepository userRepository;

    //When we call load method it calls buildUserDetails method which then map user roles
    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        User user = Optional.ofNullable(userRepository.findByEmail(email)).orElseThrow(() -> new UsernameNotFoundException("User Not Found"));
        return ShopUserDetails.buildUserDetails(user);
    }
}
