package com.jinwoo.basic.service.Implement;


import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.jinwoo.basic.dto.request.PatchNicknameRequestDto;
import com.jinwoo.basic.dto.request.PostUserRequestDto;
import com.jinwoo.basic.dto.request.SingInRequestDto;
import com.jinwoo.basic.dto.respose.DeleteUserResponseDto;
import com.jinwoo.basic.dto.respose.PatchNicknameResponseDto;
import com.jinwoo.basic.dto.respose.PostUserResponseDto;
import com.jinwoo.basic.dto.respose.ResponseDto;
import com.jinwoo.basic.dto.respose.SingInResponseDto;
import com.jinwoo.basic.entity.UserEntity;
import com.jinwoo.basic.provider.JwtProvider;
import com.jinwoo.basic.repository.UserRepository;
import com.jinwoo.basic.service.MainService;

import lombok.RequiredArgsConstructor;

//  description : @Component - 해당클래스를 java bean으로 등록하여 Spring이 인스턴스 생성(@RequiredArgsConstructor등 생성자생성하는 어노테이션)을 알아서 할 수 있도록 하는 어노테이션  //
//  description : @Service - @Component 와 동일한 작업을 수행하지만 가독성을 위해 Service라는 이름을 가짐  //
@Service
@RequiredArgsConstructor
public class MainServiceImplement implements MainService{

    private final UserRepository userRepository;
    private final JwtProvider jwtProvider;

    // description : PasswordEncoder => 비밀번호를 안전하게 암호화 하고 검증하는 인터페이스    //
    // description : BCryptPasswordEncoder => Bcrypt 해시 알고리즘을 사용하는 PasswordEncoder 구현 클래스    //
    private PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    @Override
    public String getMethod() {
        return "This method is get method. 여기서 값을 만든다.";
    }

    @Override
    public ResponseEntity<? super PostUserResponseDto> postUser(PostUserRequestDto dto) {
       //   INSERT INTO user (email, password, nickname, tel_number, address, address_detail)
       //   VALUES(dto.getEmail(),...
       
        try {
            //description : 비밀번호 암호화 작업 //
            //description : 1. dto로부터 평문의 비밀번호(암호화할 문자열)을 가져옴  //
            String password = dto.getPassword();
            //description : 2. PasswordEncoder의 인스턴스의 encode() 메서드로 평문을 암호화 //
            String emcodedPassword = passwordEncoder.encode(password);
            //description : 3. dto에 다시 주입  //
            dto.setPassword(emcodedPassword);


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

    @Override
    public ResponseEntity<? super SingInResponseDto> singIn(SingInRequestDto dto) {
        
        SingInResponseDto resposeBody = null;
        
        try {
            // description : 1. 이메일을 꺼내옴
            String email = dto.getEmail();
            // description : 2. dto로 받은 email을 이용하여 데이터베이스에서 조회   //
            UserEntity userEntity = userRepository.findByEmail(email);  
            // description : 3. email에 해당하는 레코드가 존재하는지 확인 -> 존재하지 않으면 반환 //
            if (userEntity == null)
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new ResponseDto("SF","Sing In Faild"));
            // description : 4. userEntity에서 암호화 되어있는 password를 추출 //
            String encodedPassword = userEntity.getPassword();
            // description : 5. dto에서 평문의 password 추출 //
            String password = dto.getPassword();
            // description : 6. 암호화되어있는 password와 평문의 password를 비교 (matches() 매서드는 평문의 문자열과 암호화된 문자열을 비교) //
            boolean isMatched = passwordEncoder.matches(password, encodedPassword);
            if(!isMatched)
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new ResponseDto("SF","Sing In Faild"));
            // description : 7. 토큰 생성   //
            String token = jwtProvider.create(email);
            resposeBody = new SingInResponseDto("SU", "SUCCESS", token);
            
        } catch (Exception exception) {
            exception.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ResponseDto("DBE", "Database Error"));
        }
        return ResponseEntity.status(HttpStatus.OK).body(resposeBody);
    }
    
}
