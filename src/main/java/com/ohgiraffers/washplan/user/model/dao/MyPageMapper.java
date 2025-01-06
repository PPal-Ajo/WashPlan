package com.ohgiraffers.washplan.user.model.dao;

import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface MyPageMapper {

    // 사용자 ID를 기반으로 예약 삭제
    int deleteReservationsByUserId(String userId);

    // 사용자 ID를 기반으로 문의 삭제
    int deleteInquiriesByUserId(String userId);

    // 사용자 ID를 기반으로 회원 삭제
    int deleteUserById(String userId);
}
