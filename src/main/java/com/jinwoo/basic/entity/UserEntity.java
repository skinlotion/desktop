package com.jinwoo.basic.entity;

// import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import com.jinwoo.basic.dto.request.PostUserRequestDto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

//  description : Entity - jpa를 사용할 떄 데이터베이스의 테이블과 매핑되는 JAVA객체    //

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
//  description : @Entity - 해당 클래스를 JPA Entity로 사용하겠다고 지정하는 어노테이션    //
@Entity(name = "user")
//  description : @Table - 해당 Entity클래스가 데이터베이스의 어떤 테이블고 매칭될지를 명시하는 어노테이션  (데이터테이블컬럼과 이름이 같아야함)//
@Table(name = "user")
public class UserEntity {
    //  description : @Id = Entity 클래스에서 Primary Key필드를 표시할 떄 사용되는 어노테이션   //
    @Id
    private String email;
    private String password;
    private String nickname;
    private String telNumber;
    private String address;
    private String addressDetail;
    private boolean agreedPersonal;
    private String profileImageUrl;
    //  description : @Colum = Entity 클래스의 필드와 데이터베이스 테이블의 컬럼을 명시적으로 매핑하는 어노테이션   //
    // @Column(name = "profile_image_url") // DB에있는 컬럼명을 그대로 사용해야 한다.
    // private String profile;  // 내가 사용하고 싶은 이름을 넣는다.

    public UserEntity(PostUserRequestDto dto){
        this.email = dto.getEmail();
        this.password = dto.getPassword();
        this.nickname = dto.getNickname();
        this.telNumber = dto.getTelNumber();
        this.address = dto.getAddress();
        this.addressDetail = dto.getAddressDetail();
        this.agreedPersonal = true;
    }
    
    public void updateNickname(String nickname) {
        this.nickname = nickname;
    }

}
