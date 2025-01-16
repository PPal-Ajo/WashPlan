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
            // 예약 정보 조회
            ReservationDTO reservation = myPageMapper.findReservationQRCode(reserveNo);
            
            if (reservation == null) {
                throw new RuntimeException("예약 정보를 찾을 수 없습니다.");
            }
            
            // QR 코드에 포함될 정보 구성
            StringBuilder qrData = new StringBuilder();
            qrData.append("예약번호: ").append(reservation.getReserveNo()).append("\n");
            qrData.append("회원번호: ").append(reservation.getUserNo()).append("\n");
            qrData.append("기기번호: ").append(reservation.getMachineNo()).append("\n");
            
            // null 체크를 하면서 정보 추가
            if (reservation.getReserveDate() != null) {
                qrData.append("예약일자: ").append(reservation.getReserveDate().toString()).append("\n");
            }
            if (reservation.getStartTime() != null) {
                qrData.append("시작시간: ").append(reservation.getStartTime().toString()).append("\n");
            }
            if (reservation.getEndTime() != null) {
                qrData.append("종료시간: ").append(reservation.getEndTime().toString()).append("\n");
            }
            if (reservation.getReserveOption() != null) {
                qrData.append("예약옵션: ").append(reservation.getReserveOption());
            }
            
            // QR 코드 생성
            QRCodeWriter qrCodeWriter = new QRCodeWriter();
            BitMatrix bitMatrix = qrCodeWriter.encode(qrData.toString(), BarcodeFormat.QR_CODE, 200, 200);
            
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            MatrixToImageWriter.writeToStream(bitMatrix, "PNG", outputStream);
            
            return outputStream.toByteArray();
            
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("QR 코드 생성 중 오류가 발생했습니다.", e);
        }
    }

    public ReservationDTO getReservationQRCode(int reserveNo) {
        return myPageMapper.findReservationQRCode(reserveNo);
    }
}