package com.jinwoo.basic.dto.respose;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class SingInResponseDto extends ResponseDto{
    
    private String token;
    private int expiration;

    public SingInResponseDto(String code, String message, String token) {
        super(code, message);
        this.token = token;
        this.expiration = 3600;
    }
}
