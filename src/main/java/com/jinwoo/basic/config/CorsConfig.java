package com.jinwoo.basic.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

// description : CORS - Cross-Origin Resource Sharing - '브라우저'에서 '실행되는 스크립트'가 다른 출처의 리소스에 접근하는 것을 제어하는 보안 정책  //
// description : Origin = 출처 =프로토콜, 호스트, 포트번호 / 스크립트 XMLHttpRequest 또는 Fetch 등과 같은 

// description : @Configuration -Spring 설정 작업을 하는 클래스 //

@Configuration
// description : WebMvcConfigurer - 기본적인 웹 작업에 대한 설정 인터페이스 //
public class CorsConfig implements WebMvcConfigurer {
    
    // description : WebMvcConfigurer의 default method 중 addCorsMappings로 CORS 정책 작성 //
    @Override
    public void addCorsMappings(CorsRegistry corsRegistry) {
        corsRegistry
        // description : CorsResistry 객체를 사용하여 정책 추가 //

        .addMapping("/**")
        //description : 정책을 지정할 URL 패턴 지정(여기서는 전체 패턴을 지정)
        
        .allowedMethods("*")
        //description : 특정 HTTP method에 대하여 CORS 허용(여기서는 전체 허용)

        .allowedOrigins("*");
        //description : 특정 출처에 대해서 CROS허용 (여기서는 전체를 허용)
        

    }

}
