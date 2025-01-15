package com.ohgiraffers.washplan.user.model.dao;

import com.ohgiraffers.washplan.reservation.model.dto.ReservationDTO;
import com.ohgiraffers.washplan.user.model.dto.ReservationDetailsDTO;
import org.apache.ibatis.annotations.*;
import org.apache.ibatis.type.BlobTypeHandler;

import java.util.List;

@Mapper
public interface MyPageMapper {

    // 사용자 ID를 기반으로 예약 삭제
    int deleteReservationsByUserId(String userId);

    // 사용자 ID를 기반으로 문의 삭제
    int deleteInquiriesByUserId(String userId);

    // 사용자 ID를 기반으로 회원 삭제
    int deleteUserById(String userId);


    List<ReservationDetailsDTO> findReservationsByUser(@Param("userNo") int userNo);

    // 예약 삭제
    int deleteReservationByNo(@Param("reserveNo") int reserveNo);

    byte[] getQRCode(@Param("reserveNo") int reserveNo);

    ReservationDTO findReservationQRCode(int reserveNo);

    }


