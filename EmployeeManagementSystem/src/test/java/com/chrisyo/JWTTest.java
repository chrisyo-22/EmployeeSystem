package com.chrisyo;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtParser;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.MacAlgorithm;
import org.junit.jupiter.api.Test;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import javax.crypto.SecretKey;

public class JWTTest {


//    @Test
    public void testJWT() {

        MacAlgorithm algo = Jwts.SIG.HS512; //or HS384 or HS256
// We need a signing key, so we'll create one just for this example. Usually
// the key would be read from your application configuration instead.
        Map<String, Object> claims = new HashMap<>();
        claims.put("name", "Joe");
        claims.put("admin", true);
        claims.put("id", 123456);

        SecretKey key = Jwts.SIG.HS512.key().build();

        String jws = Jwts.builder()
                .subject("Joe")
                .claims(claims)
                .expiration(new Date(System.currentTimeMillis() + 2 * 3600 * 1000)) //two hours
                .signWith(key,algo)
                .compact();

        System.out.println(jws);



        // Verify a JWS (signed token)
        Claims parsed = Jwts.parser()
                .verifyWith(key)          // supply the verification key
                .build()
                .parseSignedClaims(jws)   // parse a signed (JWS) token
                .getPayload();

        System.out.println(parsed);

    }
}
