package com.ohgiraffers.washplan.user.model.dao;


import com.ohgiraffers.washplan.user.model.dto.UserDTO;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface UserMapper {

    // 중복 이메일 확인
    @Select("SELECT COUNT(*) FROM TBL_USER WHERE email = #{email}")
    int countByEmail(@Param("email") String email);

    @Insert("INSERT INTO TBL_USER (USER_ID, USER_PWD, EMAIL, USER_STATUS, CREATED_TIME) " +
            "VALUES (#{userId}, #{password}, #{email}, #{userStatus}, NOW())")
    void insertUser(UserDTO user);

    @Select("SELECT COUNT(*) FROM TBL_USER WHERE USER_ID = #{userId}")
    int countByUserId(@Param("userId") String userId);

    @Select("SELECT USER_ID FROM TBL_USER WHERE EMAIL = #{email}")
    String getUserIdByEmail(@Param("email") String email);

    @Select("SELECT COUNT(*) > 0 FROM TBL_USER WHERE EMAIL = #{email} AND USER_ID = #{userId}")
    boolean existsByEmailAndId(@Param("email") String email, @Param("userId") String userId);



}
