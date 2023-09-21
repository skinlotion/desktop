package com.jinwoo.basic.service;

import org.springframework.http.ResponseEntity;

import com.jinwoo.basic.dto.request.PatchNicknameRequestDto;
import com.jinwoo.basic.dto.request.PostUserRequestDto;
import com.jinwoo.basic.dto.request.SingInRequestDto;
import com.jinwoo.basic.dto.respose.DeleteUserResponseDto;
import com.jinwoo.basic.dto.respose.PatchNicknameResponseDto;
import com.jinwoo.basic.dto.respose.PostUserResponseDto;
import com.jinwoo.basic.dto.respose.SingInResponseDto;

public interface MainService {
    String getMethod();
    ResponseEntity<? super PostUserResponseDto> postUser(PostUserRequestDto dto);
    ResponseEntity<? super PatchNicknameResponseDto> patchNickname(PatchNicknameRequestDto dto);
    ResponseEntity<? super DeleteUserResponseDto> deleteUser(String email);
    ResponseEntity<? super SingInResponseDto> singIn(SingInRequestDto dto);
}
