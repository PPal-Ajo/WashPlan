package com.ohgiraffers.washplan.reservation.model.service;

import com.ohgiraffers.washplan.reservation.model.dao.ReservationMapper;
import com.ohgiraffers.washplan.reservation.model.dto.ReservationDTO;
import com.ohgiraffers.washplan.machine.model.dto.MachineDTO;
import com.ohgiraffers.washplan.reservation.model.service.QRCodeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.nio.charset.StandardCharsets;

@Service
public class ReservationService {

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
            // 먼저 예약 정보 저장 (QR 코드 없이)
            reservationDTO.setQrCode(null);
            reservationMapper.insertReservation(reservationDTO);

            // QR 코드 생성 (UTF-8 인코딩 적용)
            String qrContent = new String(String.format("예약번호:%d\n사용자:%d\n기기번호:%d\n예약일:%s\n시작시간:%s\n종료시간:%s",
                    reservationDTO.getReserveNo(),
                    reservationDTO.getUserNo(),
                    reservationDTO.getMachineNo(),
                    reservationDTO.getReserveDate(),
                    reservationDTO.getStartTime(),
                    reservationDTO.getEndTime()).getBytes(StandardCharsets.UTF_8));
            
            byte[] qrCodeImage = qrCodeService.generateQRCode(qrContent, 200, 200);
            System.out.println("QR 코드 생성 완료 - 크기: " + qrCodeImage.length + " bytes");

            reservationDTO.setQrCode(qrCodeImage);
            reservationMapper.updateQRCode(reservationDTO);

            return reservationDTO;
        } catch (Exception e) {
            System.out.println("예약 저장 중 오류 발생: " + e.getMessage());
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

    public int checkReservationStatus(int machineNo, String reserveDate, String startTime, String endTime) {
        Map<String, Object> params = new HashMap<>();
        params.put("machineNo", machineNo);
        params.put("reserveDate", reserveDate);
        params.put("startTime", startTime);
        params.put("endTime", endTime);

        return reservationMapper.checkReservation(params);
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

    @Transactional
    public void handleExpiredReservations() {
        // 1. 만료된 예약 상태를 '완료'로 변경
        reservationMapper.updateReservationStatus();
        
        // 2. 완료된 예약을 히스토리로 이동
        reservationMapper.insertReservationHistory();
        
        // 3. 완료된 예약 삭제
        reservationMapper.deleteCompletedReservations();
    }

}
