package com.jinwoo.basic.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.jinwoo.basic.entity.UserEntity;

// description : Repository = 데이터 접근 계층으로 데이터베이스에 접근하여 데이터베이스 작업을 하고, 그 결과를 반환하는 영역

// description : @Repository => 어노테이션 Component의 역할을 하며, Repository임을 명시하기 위해 이름만 Repository임
@Repository
// description : JpaRepository 인터페이스 : JPA기반의 Repository 인터페이스를 구현하는데 사용되는 인터페이스
// description : JpaRepository <Type(해당 리포지토리에서 사용될 엔터티 클래스), ID(해당 엔터티 클래스에서 지정한 기본 키필드의 타입)>
public interface UserRepository extends JpaRepository<UserEntity, String> {
    
    //SELECT * FROM user WHERE email = '??';
    UserEntity findByEmail(String email);

    //SELECT * FROM user WHERE email = '??' AND nickname ='??';
    UserEntity findByEmailAndNickname(String email, String nickname);

    //SELECT * FROM user WHERE address_Detail = '??' ORDER BY address DESC ='??';
    List<UserEntity> findByAddressDetailOrderByAddressDesc (String addressDetail);

    boolean existsByEmail(String email);
    //SELECT * FROM user WHERE email = '??' OR nickname ='??' OR tel_number ='?'=> 레코드가 존재 하는지?;
    boolean existsByEmailOrNicknameOrTelNumber (String email, String nickname, String telNumber);

    long countByAddress(String address); // 동일한 조건의 갯수가 몇개가 있는지 보는 메서드

    // description : @Query : 쿼리 메서드 만으로 데이터 베이스 작업을 수행할 수 없을 때 사용하는 어노테이션 //
    // description : JPQL(Java Persistance Query Language) => SQL과 문법은 유사하지만 데이터베이스 테이블이 아닌 엔터티 클래스 기준으로 쿼리 작성   //
    // description : Native Query => SQL 기준으로 작업을 함 (SQL임)
    
    //SELECT * FROM user WHERE email = '??';
    // JPQL 방법
    @Query(value = "select u from user u where u.email = ?1")
    UserEntity findByEmailJPQL(String email);
    // Native Query 방법
    @Query(value = "SELECT * FROM user WHERE email = ?1", nativeQuery=true)
    UserEntity findByEmailSQL( String email);

    //SELECT * FROM user WHERE email = '??' AND nickname ='??';
    // JPQL 방법
    @Query(value = "select u from user u where u.email = ?1 AND  u.nickname = ?2" )
    UserEntity findByEmailAndNicknameJPQL(String email, String nickname);

    // Native Query 방법
    @Query(value = "SELECT * FROM user WHERE email = ?1 AND u.nickname = ?2", nativeQuery=true)
    UserEntity findByEmailAndNicknameSQL( String email, String nickname);

    // 
    @Query(value =
        "SELECT * " +
        "FROM user " +
        "WHERE email IN ( " +
        "SELECT DISTINCT writer_email " +
        "FROM board " +
        ")"
        , nativeQuery = true)
    List<UserEntity> getBoardWriterUserList();

}
