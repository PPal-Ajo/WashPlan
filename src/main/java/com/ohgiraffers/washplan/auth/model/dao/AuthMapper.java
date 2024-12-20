package com.ohgiraffers.washplan.auth.model.dao;


import com.ohgiraffers.washplan.user.model.dto.UserDTO;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface AuthMapper {

    @Insert("INSERT INTO TBL_USER (USER_ID, USER_PWD, EMAIL, USER_STATUS, CREATED_TIME) " +
            "VALUES (#{userId}, #{password}, #{email}, 'ACTIVE', NOW())")
    void insertUser(@Param("userId") String userId,
                    @Param("password") String password,
                    @Param("email") String email);

    @Select("SELECT USER_PWD FROM TBL_USER WHERE USER_ID = #{userId}")
    String findPasswordByUserId(@Param("userId") String userId);


    UserDTO findByUserId(@Param("userId") String userId);
}

