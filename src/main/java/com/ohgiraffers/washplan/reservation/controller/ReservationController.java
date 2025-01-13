package com.ohgiraffers.washplan.reservation.controller;

import com.ohgiraffers.washplan.auth.model.dto.CustomUserDetails;
import com.ohgiraffers.washplan.auth.model.service.CustomUserDetailsService;
import com.ohgiraffers.washplan.reservation.model.dto.ReservationDTO;
import com.ohgiraffers.washplan.reservation.model.service.ReservationService;
import com.ohgiraffers.washplan.reservation.model.service.QRCodeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Base64;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
public class ReservationController {

    private final ReservationService reservationService;
    private final QRCodeService qrCodeService;

    @Autowired
    public ReservationController(ReservationService reservationService, QRCodeService qrCodeService) {
        this.reservationService = reservationService;
        this.qrCodeService = qrCodeService;
    }

    @GetMapping("/reservation")
    public String reservation(Model model) {
        int machineNo101 = reservationService.getMachineNo101();
        int machineNo102 = reservationService.getMachineNo102();
        int machineNo103 = reservationService.getMachineNo103();
        int machineNo201 = reservationService.getMachineNo201();
        int machineNo202 = reservationService.getMachineNo202();
        int machineNo203 = reservationService.getMachineNo203();


        model.addAttribute("machineNo101", machineNo101);
        model.addAttribute("machineNo102", machineNo102);
        model.addAttribute("machineNo103", machineNo103);
        model.addAttribute("machineNo201", machineNo201);
        model.addAttribute("machineNo202", machineNo202);
        model.addAttribute("machineNo203", machineNo203);





        return "reservation/reservation";
    }

    @GetMapping("/reservation/status/{machineNo}")
    @ResponseBody
    public String getMachineStatus(@PathVariable int machineNo) {
        return reservationService.getMachineStatus(machineNo); // 기기 상태 반환
    }

    @PostMapping("/reservation/save")
    public ResponseEntity<?> saveReservation(@RequestBody ReservationDTO reservationDTO) {
        try {
            // 로그인 체크 및 예약 저장 로직
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            if (authentication != null && authentication.getPrincipal() instanceof CustomUserDetails) {
                CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
                int userNo = userDetails.getUserNo();
                reservationDTO.setUserNo(userNo);
            } else {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                        .body(Map.of("error", "로그인이 필요합니다."));
            }

            // 예약 정보 저장 및 QR 코드 생성
            ReservationDTO savedReservation = reservationService.saveReservation(reservationDTO);

            // 응답 데이터 구성
            Map<String, Object> response = new HashMap<>();
            response.put("message", "예약이 완료되었습니다.");
            response.put("qrCode", Base64.getEncoder().encodeToString(savedReservation.getQrCode()));

            return ResponseEntity.ok(response);

        } catch (Exception e) {
            e.printStackTrace(); // 콘솔에 에러 출력
            Map<String, String> errorResponse = new HashMap<>();
            errorResponse.put("error", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(errorResponse);
        }
    }

    @PostMapping("/reservation/checkStatus")
    @ResponseBody
    public Map<String, String> checkReservationStatus(@RequestBody Map<String, String> request) {
        int machineNo = Integer.parseInt(request.get("machineNo")); // String to int 변환
        String reserveDate = request.get("reserveDate");
        String startTime = request.get("startTime");
        String endTime = request.get("endTime");

        int count = reservationService.checkReservationStatus(machineNo, reserveDate, startTime, endTime);

        Map<String, String> response = new HashMap<>();
        response.put("status", count > 0 ? "예약중" : "사용가능");

        return response;
    }

    // 예약 상태 업데이트 (예약중 -> 완료)
    @PostMapping("/reservation/updateStatus")
    public ResponseEntity<String> updateReservationStatus() {
        reservationService.updateReservationStatus();
        return new ResponseEntity<>("Reservation status updated!", HttpStatus.OK);
    }








}
