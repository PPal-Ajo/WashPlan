package com.ohgiraffers.washplan.user.model.dao;


import com.ohgiraffers.washplan.user.model.dto.UserDTO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface UserMapper {
    UserDTO findByUserId(String userId);

    // 중복 이메일 확인
    @Select("SELECT COUNT(*) FROM TBL_USER WHERE email = #{email}")
    int countByEmail(@Param("email") String email);

    void insertUser(UserDTO user);

}
