package com.jinwoo.basic.dto.request;

import javax.validation.constraints.NotBlank;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class SingInRequestDto {
    @NotBlank
    private String email;
    @NotBlank
    private String password;
    
}
