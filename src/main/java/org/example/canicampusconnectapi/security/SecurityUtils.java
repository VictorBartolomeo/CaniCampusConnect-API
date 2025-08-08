package org.example.canicampusconnectapi.security;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.Map;

@Service
public class SecurityUtils implements ISecurityUtils {

    @Value("${jwt.secret}")
    String jwtSecret;

    @Override
    public String getRole(AppUserDetails userDetails) {
        return userDetails.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority).findFirst().orElse(null);
    }

    @Override
    public String generateToken(AppUserDetails userDetails) {
        return Jwts.builder()
                .setSubject(userDetails.getUsername())
                .setIssuedAt( new Date(System.currentTimeMillis()))
                .setExpiration( new Date(System.currentTimeMillis() + 1000 * 60 * 60 * 10))
                .claim("userId", userDetails.getUserId())
                .addClaims(Map.of("role", getRole(userDetails)))
                .signWith(SignatureAlgorithm.HS256, jwtSecret)
                .compact();
    }

    @Override
    public String getSubjectFromJwt(String jwt) {
        return Jwts.parser()
                .setSigningKey(jwtSecret)
                .parseClaimsJws(jwt)
                .getBody()
                .getSubject();
    }
}
