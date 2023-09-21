package com.jinwoo.basic.provider;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;

import org.springframework.stereotype.Component;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

// description : JWT를 생성 및 검증해주는 클래스    //

@Component
public class JwtProvider {

    private String secretkey = "S3cr3tkt3y";

    // description : JWT 생성 메서드    //
    public String create(String subject) {
        // description : 토큰 만료시간 작성 (현재시간부터 1시간 후)  //
        Date expiration = Date.from(Instant.now().plus(1, ChronoUnit.HOURS));

        // description : JWT 생성   //
        // description : 1. Jwts 클래스의 builder()매서드를 통해서 작성을 시작 //
        String jwt = Jwts.builder()
        // description : 2. signWith() 매서드를 통해서 서명 알고리즘 및 서명에 사용할 비밀키 지정  //
                        .signWith(SignatureAlgorithm.HS256, secretkey)
        // description : 3. setXXX() 매서드를 통해서 payload(토큰안에 들어있는 정보들) 작성  //
                            // description : setSubject() 매서드 => 생성주체(접근주제)가 누구인지 지정  //
                        .setSubject(subject)
                            // description : setIssuedAt() 매서드 => 토큰 생성 시간  //
                            .setIssuedAt(new Date())
                            // description : setExpiration() 매서드 => 토큰 만료 시간  //
                            .setExpiration(expiration)
        // description : 4. compact() 매서드를 통해서 jwt 생성완료  //
                        .compact();
        return jwt;
    }

    // description : JWT 검증 메서드    //
    
    // description : 검증 결과 과정    //
    // description : 1.jwt를 받아옴    //
    // description : 2.받아온 jwt를 우리가 알고 있는 secret key(비밀키)로 검증  //
    // description : 3.검증 완료 후 jwt에서 payload를 꺼내옴    //
    // description : 4.payload에서 원하는 데이터(subject)를 반환하는 작업    //
    public String vaildate(String jwt) {
        Claims claims = null;

        try {
            // description : 2.과정 - Jwts클래스의 parser() 매서드를 통해 파싱 시작   //
            claims = Jwts.parser()
            // description : 2.과정 - setSignkey()매서드를 통해 parser에 비밀키 등록하여 검증   //
                                .setSigningKey(secretkey)
            // description : 3.과정 - parseClaimsJws() 매서드를 통해서 파싱 처리   //
                                .parseClaimsJws(jwt)
            // description : 3.과정 - getBody() 매서드를 통해 클레임(페이로드)를 꺼내옴   //
                            .getBody();
        } catch (Exception exception) {
            exception.printStackTrace();
            return null;
        }
        
        if (claims == null) return null;

        // description : getxxx() 매서드를 통해서 원하는 정보 가져옴    //
        String subject = claims.getSubject();

        return subject;

    }
}
