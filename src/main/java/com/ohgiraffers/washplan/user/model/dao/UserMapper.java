package com.ohgiraffers.washplan.user.model.dao;


import com.ohgiraffers.washplan.user.model.dto.UserDTO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface UserMapper {

    @Select("SELECT USER_ID AS userId, USER_PWD AS password, EMAIL AS email, USER_STATUS AS userStatus, CREATED_TIME AS createdTime FROM TBL_USER WHERE USER_ID = #{userId}")
    UserDTO findByUserId(@Param("userId") String userId);

    // 중복 이메일 확인
    @Select("SELECT COUNT(*) FROM TBL_USER WHERE email = #{email}")
    int countByEmail(@Param("email") String email);

    void insertUser(UserDTO user);

}
