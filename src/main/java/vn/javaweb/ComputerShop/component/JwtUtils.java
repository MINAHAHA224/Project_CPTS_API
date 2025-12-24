package vn.javaweb.ComputerShop.component;


import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import vn.javaweb.ComputerShop.handleException.exceptions.BusinessException;
import vn.javaweb.ComputerShop.repository.UserRepository;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

import static java.lang.System.currentTimeMillis;

@Component
@RequiredArgsConstructor
public class JwtUtils {
    private final UserRepository userRepository;

    private static final String characterKey = "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQDFtY6z8Y0r6u8S5+7y1gXk5h0";

    private static final long expirationTime = 1000 * 60 * 60;


    public String generateToken(Long userId , String email , String permission) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("userId", userId);
        claims.put("permission", permission);
        return Jwts.builder()
                .setClaims(claims)
                .setSubject(email.trim())
                .setIssuedAt(new Date(currentTimeMillis()))
                .setExpiration(new Date(currentTimeMillis() + expirationTime))
                .signWith(generateKey(), SignatureAlgorithm.HS256)
                .compact();
    }


    public byte[] getBytesFromCharacterKey(){
        return Decoders.BASE64.decode(characterKey);
    }
    private Key generateKey() {
        return Keys.hmacShaKeyFor(getBytesFromCharacterKey());
    }

    public String extractUsernameFromToken(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(generateKey())
                .build()
                .parseClaimsJws(token)
                .getBody()
                .getSubject();
    }

    public Claims extractAllClaimsFromToken(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(generateKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        Claims claims = extractAllClaimsFromToken(token);
        return claimsResolver.apply(claims);
    }

    public String extractUsernameEnhanced ( String token ) {
        return extractClaim ( token , Claims::getSubject );
    }


    public Long extractExpirationTimeFromToken  ( String token ){
        return extractClaim(token , Claims::getExpiration).getTime();
    }

    public String extractPermissionEnhanced ( String token ){
        return extractClaim(token , Claims -> Claims.get("permission" , String.class) );
    }

    public boolean isTokenExpired ( String token ){
        return extractExpirationTimeFromToken(token) == null || currentTimeMillis() > extractExpirationTimeFromToken(token);
    }

    public boolean validateToken ( String token){
        final String usernameExtracted = extractUsernameEnhanced(token);
        boolean isTokenExpired = isTokenExpired(token);
        if ( isTokenExpired ){
            throw new BusinessException("Token is expired. please login again");
        }
        return
                usernameExtracted != null &&
                !usernameExtracted.isEmpty() &&
                userRepository.existsUserEntityByEmail(usernameExtracted);
    }
}
