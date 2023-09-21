package com.jinwoo.basic.config;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.jinwoo.basic.filter.JwtAuthenticationFilter;

import lombok.RequiredArgsConstructor;

//  description : 인증 및 인가 처리와 관련된 여러 설정을 지정하는 클래스 //

// description : @Configurable => Spring설정 변경이 가능한 클래스로 지정하는 어노테이션    //
@Configurable
// description : @EnableWebSecurity => Spring Security 설정 변경 클래스로 지정하는 어노테이션    //
@EnableWebSecurity
@RequiredArgsConstructor
public class WedSecurityConfig {

    private final JwtAuthenticationFilter jwtAuthenticationFilter;
                                        
    // description : Security 설정 변경 메서드 작성 //
    @Bean
    protected SecurityFilterChain configure(HttpSecurity httpSecurity) throws Exception{
        httpSecurity
            // description : CORS 정책은 기본 정책으로 사용 (우리가 만든 CorsConfig에 따르게 한다)
            .cors().and()
            // description : CSRF 보안설정은 사용하지 않음  //
            .csrf().disable()
            // description : basic 인증 사용하지 않음  //
            .httpBasic().disable()
            // description : session 생성 전략을 세션을 생성하지 않음으로 맞춤  //
            .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS).and()
            // description : 어떤 요청에 대해서 인증을 수행할지 지정하는 설정  //
            .authorizeRequests()
            // description : antMatchers() => 특정요청을 지정하는 것(어떤 요청에만 응답하겠다 이런 거) (URL 패턴에 따른 지정, http method에 따른 지정, http method + URL패턴에 따른 지정)
            // description : url 패턴에 따른 지정 (/user로 시작하는 모든 요청에 대해 허용하는 방법)
            .antMatchers("/user/**", "/sign-in").permitAll()
            // description : http method에 따른 지정 (모든 get 요청에 대해 허용하도록 하는 방법)
            .antMatchers(HttpMethod.GET).permitAll()
            // description : http method + url 패턴에 따른 지정 (POST 이면서 /board 로 시작하는 모든 요청을 허용하도록 하는 방법)
            .antMatchers(HttpMethod.POST, "/board/**").permitAll()
            // description : 나머지 모든 요청에 대해서는 인증을 진행 하겠다.
            .anyRequest().authenticated().and()
            // description : 인증과정에서 발생한 예외를 직접처리 하는 코드  //
            .exceptionHandling().authenticationEntryPoint(new FailedAuthenticationEntryPoint());

        httpSecurity.addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);

        return httpSecurity.build();
    }

}

// description : 인증과정에서 발생하는 예외에 대한 Response를 직접 처리하는 클래스  //
// description : AuthenticationEntryPoint 인터페이스 구현   //
class FailedAuthenticationEntryPoint implements AuthenticationEntryPoint {

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response,
            AuthenticationException authException) throws IOException, ServletException {
        
        // description : Response의 콘텐츠 타입을 Json으로 지정 //
        response.setContentType("application/json");
        // description : Response의 Http 상태 코드 지정 //
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        // description : Response의 Body를 작성 //
        response.getWriter().write("{\"code\" : \"AF\", \"message\" : \"Authentication Faild\"}");
    }

}