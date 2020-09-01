package wrapper.quartz.scheduler.service;

import io.jsonwebtoken.*;
import io.jsonwebtoken.impl.TextCodec;
import io.jsonwebtoken.impl.compression.DefaultCompressionCodecResolver;
import io.jsonwebtoken.impl.compression.GzipCompressionCodec;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.time.DateUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import wrapper.quartz.scheduler.entity.jpa.User;

import java.util.Date;

/**
 * The type Jwt service.
 */
@Service
@Slf4j
public class JwtService {

    @Value("${jwt.secret:MySecretJwt}")
    private String SECRET;
    @Value("${jwt.expirationTime:3600}")
    private String EXPIRATION_TIME;

    private String generateToken(User user, boolean expires) throws JwtException, IllegalArgumentException {
        Date now = new Date();
        Date expiration = expires ? DateUtils.addSeconds(now, Integer.parseInt(EXPIRATION_TIME)) : null;
        return Jwts
                .builder()
                .setClaims(Jwts
                        .claims()
                        .setIssuedAt(now)
                        .setExpiration(expiration)
                        .setSubject(user.getUsername())
                        .setId(user.getId().toString())
                        .setIssuer(user.getFullName()))
                .signWith(SignatureAlgorithm.HS512, TextCodec.BASE64.encode(SECRET))
                .compressWith(new GzipCompressionCodec())
                .compact();
    }

    /**
     * Generate access token string.
     *
     * @param user the user
     * @return the string
     * @throws JwtException             the jwt exception
     * @throws IllegalArgumentException the illegal argument exception
     */
    public String generateAccessToken(User user) throws JwtException, IllegalArgumentException {
        return generateToken(user, true);
    }

    /**
     * Generate refresh token string.
     *
     * @param user the user
     * @return the string
     * @throws JwtException             the jwt exception
     * @throws IllegalArgumentException the illegal argument exception
     */
    public String generateRefreshToken(User user) throws JwtException, IllegalArgumentException {
        return generateToken(user, false);
    }

    /**
     * Parse token jws.
     *
     * @param token the token
     * @return the jws
     * @throws JwtException             the jwt exception
     * @throws IllegalArgumentException the illegal argument exception
     */
    public Jws<Claims> parseToken(String token) throws JwtException, IllegalArgumentException {
        return Jwts
                .parser()
                .setSigningKey(TextCodec.BASE64.encode(SECRET))
                .setSigningKeyResolver(new SigningKeyResolverAdapter())
                .setCompressionCodecResolver(new DefaultCompressionCodecResolver())
                .parseClaimsJws(token);
    }
}