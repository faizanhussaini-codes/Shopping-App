package com.ShoppingCart.ShoppingCart.security.config;

import com.ShoppingCart.ShoppingCart.security.jwt.AuthTokenFilter;
import com.ShoppingCart.ShoppingCart.security.jwt.CustomAuthEntryPoint;
import com.ShoppingCart.ShoppingCart.security.user.ShopUserDetailsService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import java.util.List;

@EnableWebSecurity
@EnableMethodSecurity(prePostEnabled = true)
@Configuration
@RequiredArgsConstructor
public class ShopConfig {

    private final ShopUserDetailsService userDetailsService;
    private final CustomAuthEntryPoint authEntryPoint;
    private final AuthTokenFilter authTokenFilter;
    private static final List<String> UNSECURED_URLS = List.of("/api/v1/auth/**" );

    @Bean
    public ModelMapper modelMapper(){
        return new ModelMapper();
    }

    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authConfig) {
        return authConfig.getAuthenticationManager();
    }

    @Bean
    public DaoAuthenticationProvider daoAuthenticationProvider(){
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider(userDetailsService);
        authProvider.setPasswordEncoder(passwordEncoder());
        return authProvider;
    }

    //Defines security rules & filters
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http){
        http.csrf(AbstractHttpConfigurer::disable)//CSRF (Cross-Site Request Forgery) is a security protection that: Prevents unauthorized POST/PUT/DELETE requests from other sites
                .exceptionHandling(exception -> exception.authenticationEntryPoint(authEntryPoint)) //Whenever an authentication error happens, Spring Security will call CustomAuthEntryPoint.commence(...) and return THIS JSON response format.
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))//No session stored on server Every request is treated independently
                .authorizeHttpRequests(auth -> auth.requestMatchers(UNSECURED_URLS.toArray(String[]::new)).permitAll()// Allow this http requests
                        .anyRequest().authenticated()); //Authenticate other request
        http.authenticationProvider(daoAuthenticationProvider());
        http.addFilterBefore(authTokenFilter, UsernamePasswordAuthenticationFilter.class);
        return http.build();

    }

}
