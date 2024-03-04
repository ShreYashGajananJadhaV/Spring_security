package com.example.JWT_Application.JWT_Application.JWT;

import com.example.JWT_Application.JWT_Application.user.User;
import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Base64;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Service
public class JWTService {

    private String secret_key = "bc1fb5e2cae890a57d695864a7c0fac3fcd5aa4e09bfd075ac733950fc92db58";

    public String extractUsername(String token){
        return extractClaims(token, Claims::getSubject);
    }

    public String generateToken(User user){
        return generateToken(new HashMap<>(),user);
    }
    public String generateToken(
            Map<String, Object> extraClaims,
            UserDetails userDetails
    ){
        return Jwts.builder()
                .setClaims(extraClaims)
                .setSubject(userDetails.getUsername())
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis()+(1000*60*24)))
                .signWith(getSignInKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    public boolean isTokenValid(String token, UserDetails user){
        final String username = extractUsername(token);
        return (username.equals(user.getUsername()) && isTokenNotExpired(token));
    }

    public boolean isTokenNotExpired(String token){
        Date expDate = extractExpiration(token);
        Date todaysDate = new Date(System.currentTimeMillis());
        return expDate.after(todaysDate);
    }
    public Date extractExpiration(String token){
        return extractClaims(token, Claims::getExpiration);
    }

    private Claims extractAllClaims(String token){
        JwtParser jwtParser = Jwts
                .parser()
                .setSigningKey(getSignInKey())
                .build();
        Jws<Claims> jws = jwtParser.parseClaimsJws(token);
        return jws.getBody();

    }


    public <T> T extractClaims(String token, Function<Claims,T> claimsResolver){
        Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    private Key getSignInKey() {
        byte[] keyBytes = Decoders.BASE64.decode(secret_key);
        return Keys.hmacShaKeyFor(keyBytes);
    }


}
