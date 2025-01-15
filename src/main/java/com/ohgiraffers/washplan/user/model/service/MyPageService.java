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
import java.io.ByteArrayOutputStream;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;

@Service
@Transactional
public class MyPageService {

    private final MyPageMapper myPageMapper;
    private final ReservationMapper reservationMapper;

    @Autowired
    public MyPageService(MyPageMapper myPageMapper, ReservationMapper reservationMapper) {
        this.myPageMapper = myPageMapper;
        this.reservationMapper = reservationMapper;
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

    @Transactional
    public boolean deleteReservation(int reserveNo) {
        int rowsDeleted = myPageMapper.deleteReservationByNo(reserveNo);
        return rowsDeleted > 0; // 삭제 성공 여부 반환
    }

    public byte[] getQRCode(int reserveNo) {
        try {
            System.out.println("서비스 계층 - QR 코드 조회 시작: " + reserveNo);
            
            // 실제 DB에서 조회하는 대신 임시 QR 코드 생성
            String dummyData = "예약번호: " + reserveNo;
            QRCodeWriter qrCodeWriter = new QRCodeWriter();
            BitMatrix bitMatrix = qrCodeWriter.encode(dummyData, BarcodeFormat.QR_CODE, 200, 200);
            
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            MatrixToImageWriter.writeToStream(bitMatrix, "PNG", outputStream);
            
            byte[] qrCode = outputStream.toByteArray();
            System.out.println("서비스 계층 - 더미 QR 코드 생성 완료: " + qrCode.length + " bytes");
            
            return qrCode;
            
        } catch (Exception e) {
            System.out.println("서비스 계층 - QR 코드 생성 실패: " + e.getMessage());
            e.printStackTrace();
            throw new RuntimeException("QR 코드 생성 중 오류가 발생했습니다.", e);
        }
    }

    public ReservationDTO getReservationQRCode(int reserveNo) {
        return myPageMapper.findReservationQRCode(reserveNo);
    }
}