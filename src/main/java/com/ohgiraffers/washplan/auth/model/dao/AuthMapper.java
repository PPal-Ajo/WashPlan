package com.ohgiraffers.washplan.auth.model.dao;


import com.ohgiraffers.washplan.admin.model.dto.AdminAuthDTO;
import com.ohgiraffers.washplan.admin.model.dto.AdminDTO;
import com.ohgiraffers.washplan.user.model.dto.UserDTO;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface AuthMapper {
    

    @Select("SELECT USER_PWD FROM TBL_USER WHERE USER_ID = #{userId}")
    String findPasswordByUserId(@Param("userId") String userId);


    UserDTO findByUserId(@Param("userId") String userId);

    // TBL_USER에서 사용자 정보 조회
    @Select("SELECT USER_NO AS userNo, USER_ID AS userId, USER_PWD AS password, EMAIL AS email " +
            "FROM TBL_USER WHERE USER_ID = #{userId}")
    UserDTO findUserByUserId(@Param("userId") String userId);

    // TBL_ADMIN에서 관리자 정보 조회
    @Select("SELECT ADMIN_NO AS adminNo, ADMIN_ID AS adminId, ADMIN_PWD AS adminPwd " +
            "FROM TBL_ADMIN WHERE ADMIN_ID = #{adminId}")
    AdminAuthDTO findAdminByAdminId(@Param("adminId") String adminId);



}

