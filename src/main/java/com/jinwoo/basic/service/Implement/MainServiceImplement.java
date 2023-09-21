package com.jinwoo.basic.service.Implement;


import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.jinwoo.basic.dto.request.PatchNicknameRequestDto;
import com.jinwoo.basic.dto.request.PostUserRequestDto;
import com.jinwoo.basic.dto.respose.DeleteUserResponseDto;
import com.jinwoo.basic.dto.respose.PatchNicknameResponseDto;
import com.jinwoo.basic.dto.respose.PostUserResponseDto;
import com.jinwoo.basic.dto.respose.ResponseDto;
import com.jinwoo.basic.entity.UserEntity;
import com.jinwoo.basic.repository.UserRepository;
import com.jinwoo.basic.service.MainService;

import lombok.RequiredArgsConstructor;

//  description : @Component - 해당클래스를 java bean으로 등록하여 Spring이 인스턴스 생성(@RequiredArgsConstructor등 생성자생성하는 어노테이션)을 알아서 할 수 있도록 하는 어노테이션  //
//  description : @Service - @Component 와 동일한 작업을 수행하지만 가독성을 위해 Service라는 이름을 가짐  //
@Service
@RequiredArgsConstructor
public class MainServiceImplement implements MainService{

    private final UserRepository userRepository;

    @Override
    public String getMethod() {
        return "This method is get method. 여기서 값을 만든다.";
    }

    @Override
    public ResponseEntity<? super PostUserResponseDto> postUser(PostUserRequestDto dto) {
       //   INSERT INTO user (email, password, nickname, tel_number, address, address_detail)
       //   VALUES(dto.getEmail(),...
       
        try {
            // description : Create 작업 순서(INSERT)    //
            // description : 1. Entity 인스턴스 생성    //
            UserEntity userEntity = new UserEntity(dto);
            // description : 2. repository의 save메서드 사용    //
            userRepository.save(userEntity);
        } catch (Exception exception) {
            exception.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new PostUserResponseDto("DBE", "faild"));
        }

       

       return ResponseEntity.status(HttpStatus.OK).body(new PostUserResponseDto("SU", "SUCCESS"));
    }

    @Override
    public ResponseEntity<? super PatchNicknameResponseDto> patchNickname(PatchNicknameRequestDto dto) {
        // 원래 SQL에서 업데이트 할려면 => UPDATE user SET nickname = dto.getNickname() WHERE eamil = dto.getEmail();

        try {
            // description : Update 작업 순서(UPDATE)    //

            // description : 1. Entity 인스턴스 조회    //
            // description : findById() : primary key를 사용하여 레코드를 검색하는 메서드   //
            // description : findById() 메서드 기능  : SELECT * FROM user WHERE email =??; 
            UserEntity userEntity = userRepository.findById(dto.getEmail()).get();

            // description : 2. Entity 인스턴스 수정    //
            userEntity.updateNickname(dto.getNickname());

            // description : 3. repository의 save 매서드 사용 //
            // description : save() 메서드 => Entity 객체를 테이블에 저장하는 매서드, 만약 해당 인스턴스의 ID 값과 동일한 레코드가 존재하면 해당 레코드를 수정(덮어쓰움) 
            // description : 그렇지 않다면 레코드를 생성
            userRepository.save(userEntity);

        } catch (Exception exception) {
            exception.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ResponseDto("DBE", "Database Error"));

        }

        return ResponseEntity.status(HttpStatus.OK).body(new PatchNicknameResponseDto("SU", "SUCCESS" ));
    }

    @Override
    public ResponseEntity<? super DeleteUserResponseDto> deleteUser(String email) {
        // 원래 SQL에서 업데이트 할려면 => DELETE FROM user WHERE email = email;
        try {
            //  description : Delete작업 순서 (DELETE)  //

            // description : 1. repository의 deleteById 메서드 사용    //
            userRepository.deleteById(email);


        } catch (Exception exception) {
            exception.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ResponseDto("DBE", "Database Error"));
        }
        return ResponseEntity.status(HttpStatus.OK).body(new DeleteUserResponseDto("SU", "SUCCESS" ));
    }
    
}
