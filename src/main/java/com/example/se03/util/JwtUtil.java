package com.example.se03.util;

import com.example.se03.dto.UserDTO;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.io.Serializable;
import java.sql.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Component
@PropertySource(ignoreResourceNotFound = true,
value = "classpath:otherprops.properties")

public class JwtUtil implements Serializable {
    private static final long serialVersionUTD = 234234523523L;
    public static final long JWT_TOKEN_VALIDITY = 24 * 60 * 60 * 12;
    @Value("${jwt.secret}")
    private String secretKey;

    public String getUsernameFromToken(String token) {
        return getClaimFromToken(token, Claims::getSubject);
    }

    public Claims getUserRolesCodeFromtoken(String token){
        return Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token).getBody();
    }

//    public Date getExpirationDateFromToken(String token){
//        return (Date) getClaimFromToken(token,Claims::getExpiration);
//    }
public java.util.Date getExpirationDateFromToken(String token) {
    return getClaimFromToken(token, Claims::getExpiration);
}

    public <T> T getClaimFromToken(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = getAllClaimsFromToken(token);
        return claimsResolver.apply(claims);
    }

    private Claims getAllClaimsFromToken(String token){
        return Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token).getBody();
    }

//    private Boolean isTokenexpired(String token){
//        final Date expiration = getExpirationDateFromToken(token);
//        return expiration.before(new java.util.Date());
//    }
private Boolean isTokenExpired(String token) {
    final java.util.Date expiration = getExpirationDateFromToken(token);
    return expiration.before(new java.util.Date());
}


    public String genarateToken(UserDTO userDTO){
        Map<String, Object> claims = new HashMap<>();
        claims.put("role", userDTO.getRole());
        return doGenarateToken(claims,
                userDTO.getEmail());
    }

//    private String doGenarateToken(Map<String, Object> claims, String subject){
//        return Jwts.builder().setClaims(claims).setSubject(subject)
//                .setIssuedAt(new Date(System.currentTimeMillis()))
//                .setExpiration(new Date(System.currentTimeMillis() + JWT_TOKEN_VALIDITY * 1000))
//                .signWith(SignatureAlgorithm.ES512,
//                        secretKey).compact();
//    }
private String doGenarateToken(Map<String, Object> claims, String subject){
    return Jwts.builder().setClaims(claims).setSubject(subject)
            .setIssuedAt(new Date(System.currentTimeMillis()))
            .setExpiration(new Date(System.currentTimeMillis() + JWT_TOKEN_VALIDITY * 1000))
            .signWith(SignatureAlgorithm.HS512, secretKey)  // Use HS512 or HS256
         .compact();
}

    public boolean validateToken (String token, UserDetails userDetails) {
        final String username = getUsernameFromToken(token);
        return (username.equals(userDetails.getUsername()) && !isTokenExpired(token));
    }
}
