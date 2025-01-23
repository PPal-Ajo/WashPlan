package com.ohgiraffers.washplan.reservation.model.service;

import com.ohgiraffers.washplan.reservation.model.dao.ReservationMapper;
import com.ohgiraffers.washplan.reservation.model.dto.ReservationDTO;
import com.ohgiraffers.washplan.machine.model.dto.MachineDTO;
import com.ohgiraffers.washplan.reservation.model.service.QRCodeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

@Service
@EnableScheduling
public class ReservationService {

    private static final Logger log = LoggerFactory.getLogger(ReservationService.class);

    private final ReservationMapper reservationMapper;
    private final QRCodeService qrCodeService;

    @Autowired
    public ReservationService(ReservationMapper reservationMapper, QRCodeService qrCodeService) {
        this.reservationMapper = reservationMapper;
        this.qrCodeService = qrCodeService;
    }

    @Transactional
    public ReservationDTO saveReservation(ReservationDTO reservationDTO) throws Exception {
        try {
            // 먼저 예약 정보 저장
            reservationDTO.setQrCode(null);
            reservationMapper.insertReservation(reservationDTO);

            // QR 코드에는 예약 번호만 포함
            String qrContent = String.valueOf(reservationDTO.getReserveNo());
            log.info("생성된 예약 번호: " + qrContent);  // 디버깅용 로그 추가
            
            byte[] qrCodeImage = qrCodeService.generateQRCode(qrContent, 200, 200);
            System.out.println("QR 코드 생성 완료 - 크기: " + qrCodeImage.length + " bytes");

            reservationDTO.setQrCode(qrCodeImage);
            reservationMapper.updateQRCode(reservationDTO);

            return reservationDTO;
        } catch (Exception e) {
            log.error("예약 저장 중 오류 발생: " + e.getMessage());
            e.printStackTrace();
            throw e;
        }
    }

    public List<Map<String, Object>> getReservations(int machineNo) {
        return reservationMapper.findReservations(machineNo);
    }

    public int getMachineNo101() {
        return reservationMapper.findMachineNo101();
    }

    public int getMachineNo102() {
        return reservationMapper.findMachineNo102();
    }

    public int getMachineNo103() {
        return reservationMapper.findMachineNo103();
    }

    public int getMachineNo201() {
        return reservationMapper.findMachineNo201();
    }

    public int getMachineNo202() {
        return reservationMapper.findMachineNo202();
    }

    public int getMachineNo203() {
        return reservationMapper.findMachineNo203();
    }

    public void updateReservationStatus() {
        reservationMapper.updateReservationStatus();
    }

    public String checkReservationStatus(int machineNo, String reserveDate, String startTime, String endTime) {
        log.info("=== 예약 상태 체크 시작 ===");
        log.info("파라미터 확인 - machineNo: {}, reserveDate: {}, startTime: {}, endTime: {}", 
                 machineNo, reserveDate, startTime, endTime);

        // 1. 먼저 현재 사용중인 예약이 있는지 확인
        ReservationDTO currentReservation = reservationMapper.getCurrentReservation(machineNo, reserveDate, startTime);
        log.info("Current Reservation details for machine {}: {}", machineNo, 
                 currentReservation != null ? 
                 String.format("ReserveNo: %d, Status: %s, Date: %s", 
                             currentReservation.getReserveNo(), 
                             currentReservation.getReserveStatus(),
                             currentReservation.getReserveDate()) : "null");
        
        // 사용중인 예약이 있다면 '사용중' 반환
        if (currentReservation != null && "사용중".equals(currentReservation.getReserveStatus())) {
            log.info("사용중인 예약 발견 -> '사용중' 상태 반환");
            return "사용중";
        }

        // 2. 아니라면 기존 예약 체크 로직 실행
        Map<String, Object> params = new HashMap<>();
        params.put("machineNo", machineNo);
        params.put("reserveDate", reserveDate);
        params.put("startTime", startTime);
        params.put("endTime", endTime);

        int reservationCount = reservationMapper.checkReservation(params);
        log.info("Reservation count for machine {}: {}", machineNo, reservationCount);
        
        String status = reservationCount > 0 ? "예약중" : "사용가능";
        log.info("최종 반환 상태: {}", status);
        log.info("=== 예약 상태 체크 종료 ===");
        
        return status;
    }

    public String getStatus101() {
        return reservationMapper.findMachineStatus101();
    }

    public String getStatus102() {
        return reservationMapper.findMachineStatus102();
    }

    public String getStatus103() {
        return reservationMapper.findMachineStatus103();
    }

    public String getMachineStatus(int machineNo) {
        return reservationMapper.getMachineStatus(machineNo);
    }

    public List<MachineDTO> getMachinesByType(String machineType) {
        return reservationMapper.findMachinesByType(machineType);
    }
    @Scheduled(fixedRate = 60000)
    @Transactional
    public void handleExpiredReservations() {
        // 1. 만료된 예약 상태를 '완료'로 변경
        reservationMapper.updateReservationStatus();
        
        // 2. 완료된 예약을 히스토리로 이동
        reservationMapper.insertReservationHistory();
        
        // 3. 완료된 예약 삭제
        reservationMapper.deleteCompletedReservations();
    }

    @Transactional(readOnly = true)
    public ReservationDTO findReservationByNo(int reserveNo) {
        log.info("[ReservationService] findReservationByNo : " + reserveNo);
        
        // 디버깅을 위한 로그 추가
        ReservationDTO reservation = reservationMapper.selectReservationByNo(reserveNo);
        if (reservation == null) {
            log.error("예약 정보를 찾을 수 없음. 예약번호: " + reserveNo);
        } else {
            log.info("조회된 예약 정보: " + reservation);
        }
        
        return reservation;
    }

    @Transactional
    public void updateQRScanStatus(int reserveNo) {
        log.info("=== QR 스캔 상태 업데이트 시작 ===");
        log.info("[ReservationService] updateQRScanStatus : " + reserveNo);
        
        // 예약 상태를 '사용중'으로 업데이트
        int result = reservationMapper.updateReservationStatusToUsing(reserveNo);
        log.info("업데이트 쿼리 실행 결과: {}", result);
        
        if(result <= 0) {
            log.error("예약 상태 업데이트 실패");
            throw new RuntimeException("예약 상태 업데이트에 실패했습니다.");
        }
        
        log.info("[ReservationService] 예약 상태가 성공적으로 업데이트되었습니다. 예약번호: " + reserveNo);
        log.info("=== QR 스캔 상태 업데이트 종료 ===");
    }

    @Transactional(readOnly = true)
    public String getCurrentReservationStatus(int machineNo) {
        log.info("[ReservationService] getCurrentReservationStatus : " + machineNo);
        
        // 현재 날짜와 시간 가져오기
        LocalDate currentDate = LocalDate.now();
        LocalTime currentTime = LocalTime.now();
        String selectedDate = currentDate.toString();
        String selectedTime = currentTime.format(DateTimeFormatter.ofPattern("HH:mm:ss"));  // 초(ss) 추가
        
        // 1. 먼저 현재 사용중인 예약이 있는지 확인
        ReservationDTO currentReservation = reservationMapper.getCurrentReservation(machineNo, selectedDate, selectedTime);
        
        if (currentReservation != null) {
            log.info("조회된 예약 정보: reserveNo={}, status={}, date={}, startTime={}, endTime={}", 
                currentReservation.getReserveNo(),
                currentReservation.getReserveStatus(),
                currentReservation.getReserveDate(),
                currentReservation.getStartTime(),
                currentReservation.getEndTime()
            );
            return currentReservation.getReserveStatus();
        }
        
        log.info("현재 예약 없음");
        return "사용가능";
    }

    @Scheduled(fixedRate = 60000)  
    @Transactional
    public void handleUnusedReservations() {
        log.info("=== 미사용 예약 처리 시작 ===");
        
        // 먼저 취소될 예약의 사용자 번호들을 조회
        List<Integer> userNos = reservationMapper.findUnusedReservationUserNos();
        
        if (!userNos.isEmpty()) {
            log.info("취소 대상 예약 발견: {} 건", userNos.size());
            
            // 각 사용자의 취소 횟수 증가
            reservationMapper.updateUserCancelCount();
            log.info("사용자 취소 횟수 증가 완료");
            
            // 예약 삭제
            reservationMapper.deleteUnusedReservation();
            log.info("미사용 예약 삭제 완료");
        }
        
        log.info("=== 미사용 예약 처리 종료 ===");
    }

    public void updateReservationStatusToUsing(int reserveNo) {
        reservationMapper.updateReservationStatusToUsing(reserveNo);
    }
}
