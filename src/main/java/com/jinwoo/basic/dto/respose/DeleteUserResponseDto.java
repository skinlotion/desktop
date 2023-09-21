package com.jinwoo.basic.dto.respose;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class DeleteUserResponseDto extends ResponseDto{
    
    public DeleteUserResponseDto (String code, String message) {
        super(code, message);
    }
    
}
