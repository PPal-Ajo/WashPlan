package com.ohgiraffers.washplan.user.model.service;

import com.ohgiraffers.washplan.reservation.model.dao.ReservationMapper;
import com.ohgiraffers.washplan.reservation.model.dto.ReservationDTO;
import com.ohgiraffers.washplan.user.model.dao.MyPageMapper;
import com.ohgiraffers.washplan.user.model.dto.ReservationDetailsDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.List;

@Service
public class MyPageService {

    private final MyPageMapper myPageMapper;

    @Autowired
    public MyPageService(MyPageMapper myPageMapper) {
        this.myPageMapper = myPageMapper;
    }

    @Transactional
    public void deleteUserAndRelatedData(String userId) {
        try {
            // 예약 데이터 삭제
            int deletedReservations = myPageMapper.deleteReservationsByUserId(userId);
            System.out.println("삭제된 예약 수: " + deletedReservations);

            // 문의 데이터 삭제
            int deletedInquiries = myPageMapper.deleteInquiriesByUserId(userId);
            System.out.println("삭제된 문의 수: " + deletedInquiries);

            // 회원 데이터 삭제
            int deletedUser = myPageMapper.deleteUserById(userId);
            if (deletedUser == 0) {
                throw new IllegalArgumentException("사용자를 찾을 수 없습니다. ID: " + userId);
            }
            System.out.println("삭제된 사용자 수: " + deletedUser);

        } catch (Exception e) {
            // 예외 발생 시 트랜잭션 롤백
            System.err.println("회원 탈퇴 중 오류 발생: " + e.getMessage());
            throw e;
        }
    }

    public List<ReservationDetailsDTO> getUserReservations(int userNo) {
        // username을 기반으로 예약 데이터를 조회
        List<ReservationDetailsDTO> reservations = myPageMapper.findReservationsByUser(userNo);
        return reservations != null ? reservations : Collections.emptyList();
    }
}