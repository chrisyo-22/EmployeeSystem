package com.chrisyo.utils;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.MacAlgorithm;

import javax.crypto.SecretKey;
import java.util.Date;

public class JwtUtils {

    // We need a signing key, so we'll create one just for this example. Usually
    // the key would be read from your application configuration instead.


    private static final String secret = "BASE64SECRETFROMCONFdsadsandhlskoahdoisajgpioajsuilodhaposdkpsaioijgoiasjdopsajopidjsaoidjsaoughdauiohgfdsIG";
    private static final Long expire = 7200000L; //two hours in a millisecond
    private static final SecretKey secretKey = Keys.hmacShaKeyFor(Decoders.BASE64.decode(secret));
    public JwtUtils(){
    }

    public static String createJwtToken(Claims claims) {
        MacAlgorithm algo = Jwts.SIG.HS512; //or HS384 or HS256


        //  byte[] keyBytes = key.getEncoded();
        //  System.out.println("Key (Base64): " + Base64.getEncoder().encodeToString(keyBytes));
        //  Only if the original secret was UTF-8 text:
        //  System.out.println("Key (UTF-8, may be gibberish): " + new String(keyBytes, StandardCharsets.UTF_8));
        String jws = Jwts.builder()
                .subject("LoginToken")
                .claims(claims)
                .expiration(new Date(System.currentTimeMillis() + expire)) //two hours
                .signWith(secretKey,algo)
                .compact();
        return jws;
    }

    public static Claims parseJwt(String jwt){
        Claims parsed = Jwts.parser()
                .verifyWith(secretKey)          // supply the verification key
                .build()
                .parseSignedClaims(jwt)   // parse a signed (JWS) token
                .getPayload();
        return parsed;
    }
}
