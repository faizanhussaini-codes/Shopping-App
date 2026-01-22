package com.ShoppingCart.ShoppingCart.security.jwt;

import com.ShoppingCart.ShoppingCart.security.user.ShopUserDetails;
import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SignatureException;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Date;
import java.util.List;

@Component
public class JwtUtil {

    @Value("${auth.token.jwtSecret}")
    private String jwtSecret;

    @Value("${auth.token.expirationMs}")
    private Long expirationTime;

    public String generateTokenForUser(Authentication authentication){
            ShopUserDetails user = (ShopUserDetails) authentication.getPrincipal(); // Get The Logged-In User
        List<String> roles = user.getAuthorities() //Extract all the roles a user has As one to many relationship
                .stream()
                .map(GrantedAuthority::getAuthority) //Now you convert them back to String for JWT, API responses, frontend, logs, or business logic
                .toList();

        return Jwts.builder() //empty token shell.
                .setSubject(user.getEmail()) //subject = who this token belongs to Later, Spring Security will extract this email and load the user.
                .claim("id", user.getId()) //Adds custom data into the token.
                .claim("roles" , roles)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + expirationTime))
                .signWith(key(), SignatureAlgorithm.HS256)
                .compact();//converts everything into a JWT string
    }

    //We convert the secret key to bytes because cryptographic algorithms only work with raw binary data, not Strings.
    //    private Key key (){
    //        return Keys.hmacShaKeyFor(Decoders.BASE64.decode(jwtSecret));
    //    }

    private Key key() {
        return Keys.hmacShaKeyFor(
                jwtSecret.getBytes(StandardCharsets.UTF_8)
        );}

    public String getUsernameFromToken(String token){
        return Jwts.parserBuilder()
                .setSigningKey(key())//Only accept the token if it was signed using my secret key
                .build()
                .parseClaimsJws(token) //This single line does multiple checks automatically: ✅ verifies signature ✅ checks token format ✅ checks expiration ✅ validates integrity
                .getBody().getSubject(); //Now that token is trusted, you safely read the data.
    }

    public Boolean validateToken(String token){
        try {
            Jwts.parserBuilder()
                    .setSigningKey(key())
                    .build()
                    .parseClaimsJws(token);
            return true;
        } catch (ExpiredJwtException | UnsupportedJwtException | MalformedJwtException | SignatureException | IllegalArgumentException e) {
            throw new JwtException(e.getMessage());
        }
    }
}
