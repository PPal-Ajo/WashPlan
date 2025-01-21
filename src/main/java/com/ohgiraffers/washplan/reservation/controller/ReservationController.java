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
import com.ohgiraffers.washplan.machine.model.dto.MachineDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Base64;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.ArrayList;

@Controller
public class ReservationController {

    private static final Logger log = LoggerFactory.getLogger(ReservationController.class);

    private final ReservationService reservationService;
    private final QRCodeService qrCodeService;

    @Autowired
    public ReservationController(ReservationService reservationService, QRCodeService qrCodeService) {
        this.reservationService = reservationService;
        this.qrCodeService = qrCodeService;
    }

    @GetMapping("/reservation")
    public String reservation(Model model) {
        List<MachineDTO> washingMachines = reservationService.getMachinesByType("세탁기");
        List<MachineDTO> dryingMachines = reservationService.getMachinesByType("건조기");

        // 두 리스트를 하나로 합침
        List<MachineDTO> allMachines = new ArrayList<>();
        allMachines.addAll(washingMachines);
        allMachines.addAll(dryingMachines);

        model.addAttribute("washingMachines", washingMachines);
        model.addAttribute("dryingMachines", dryingMachines);
        model.addAttribute("allMachines", allMachines);  // 합친 리스트 추가

        log.info("Washing Machines: {}", washingMachines);
        log.info("Drying Machines: {}", dryingMachines);

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
        log.info("=== 예약 상태 체크 요청 ===");
        log.info("요청 데이터: {}", request);

        int machineNo = Integer.parseInt(request.get("machineNo"));
        String reserveDate = request.get("reserveDate");
        String startTime = request.get("startTime");
        String endTime = request.get("endTime");

        String status = reservationService.checkReservationStatus(machineNo, reserveDate, startTime, endTime);
        log.info("반환되는 상태: {}", status);

        Map<String, String> response = new HashMap<>();
        response.put("status", status);

        log.info("=== 예약 상태 체크 응답 완료 ===");
        return response;
    }

    // 예약 상태 업데이트 (예약중 -> 완료)
    @PostMapping("/reservation/updateStatus")
    public ResponseEntity<String> updateReservationStatus() {
        reservationService.updateReservationStatus();
        return new ResponseEntity<>("Reservation status updated!", HttpStatus.OK);
    }

    @PostMapping("/reservation/check-expired")
    public ResponseEntity<String> checkExpiredReservations() {
        reservationService.handleExpiredReservations();
        return ResponseEntity.ok("Completed");
    }

    @PostMapping("/reservation/qr-scan/{reserveNo}")
    @ResponseBody
    public ResponseEntity<Map<String, Object>> updateReservationStatusAfterQRScan(@PathVariable int reserveNo) {
        Map<String, Object> response = new HashMap<>();
        
        try {
            // 로그인 체크
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            if (!(authentication != null && authentication.getPrincipal() instanceof CustomUserDetails)) {
                response.put("success", false);
                response.put("message", "로그인이 필요합니다.");
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
            }

            // 예약 정보 조회
            ReservationDTO reservation = reservationService.findReservationByNo(reserveNo);
            
            if (reservation == null) {
                response.put("success", false);
                response.put("message", "유효하지 않은 예약입니다.");
                return ResponseEntity.badRequest().body(response);
            }
            
            if (!"예약중".equals(reservation.getReserveStatus())) {
                response.put("success", false);
                response.put("message", "이미 처리된 예약이거나 취소된 예약입니다.");
                return ResponseEntity.badRequest().body(response);
            }
            
            // 예약 상태 업데이트
            reservationService.updateQRScanStatus(reserveNo);
            
            // 업데이트된 예약 정보 다시 조회
            ReservationDTO updatedReservation = reservationService.findReservationByNo(reserveNo);
            
            response.put("success", true);
            response.put("message", "QR 코드 스캔이 완료되었습니다.");
            response.put("reservation", updatedReservation);  // 업데이트된 예약 정보 전달
            response.put("machineNo", updatedReservation.getMachineNo());  // 기기 번호도 전달
            
            return ResponseEntity.ok(response);
            
        } catch (Exception e) {
            log.error("QR 코드 스캔 처리 중 오류 발생: ", e);
            response.put("success", false);
            response.put("message", "처리 중 오류가 발생했습니다: " + e.getMessage());
            return ResponseEntity.badRequest().body(response);
        }
    }

    @GetMapping("/reservation/qr-scan/{reserveNo}")
    public String handleQRScanFromMobile(@PathVariable int reserveNo, Model model) {
        try {
            // 예약 정보 조회 (로그인 체크 없이)
            ReservationDTO reservation = reservationService.findReservationByNo(reserveNo);
            
            if (reservation == null) {
                model.addAttribute("error", "유효하지 않은 예약입니다.");
                return "reservation/error";
            }
            
            if (!"예약중".equals(reservation.getReserveStatus())) {
                model.addAttribute("error", 
                    "이미 처리된 예약이거나 취소된 예약입니다. (현재 상태: " + reservation.getReserveStatus() + ")");
                return "reservation/error";
            }
            
            // 예약 상태 업데이트 전에 현재 상태 다시 한번 확인
            ReservationDTO currentStatus = reservationService.findReservationByNo(reserveNo);
            if (!"예약중".equals(currentStatus.getReserveStatus())) {
                model.addAttribute("error", "예약 상태가 변경되었습니다.");
                return "reservation/error";
            }
            
            // 예약 상태 업데이트 (로그인 체크 없이)
            reservationService.updateQRScanStatus(reserveNo);
            
            // 성공 메시지와 기기 번호 추가
            model.addAttribute("success", "QR 코드 스캔이 완료되었습니다. 이용을 시작하세요.");
            model.addAttribute("machineNo", reservation.getMachineNo());
            
            return "reservation/success";
            
        } catch (Exception e) {
            log.error("QR 스캔 처리 중 오류: ", e);
            model.addAttribute("error", "처리 중 오류가 발생했습니다.");
            return "reservation/error";
        }
    }
    @GetMapping("/reservation/current/{machineNo}")
    @ResponseBody
    public String getCurrentReservationStatus(@PathVariable int machineNo) {
        log.info("[ReservationController] getCurrentReservationStatus : machineNo={}", machineNo);
        
        return reservationService.getCurrentReservationStatus(machineNo);
    }

    @GetMapping("/reservation/success")
    public String showSuccess() {
        return "reservation/success";
    }

    @GetMapping("/reservation/error")
    public String showError() {
        return "reservation/error";
    }

    @PostMapping("/reservation/check-unused")
    public ResponseEntity<String> checkUnusedReservations() {
        log.info("=== 미사용 예약 체크 요청 ===");
        try {
            reservationService.handleUnusedReservations();
            log.info("=== 미사용 예약 체크 완료 ===");
            return ResponseEntity.ok("Completed");
        } catch (Exception e) {
            log.error("미사용 예약 처리 중 오류 발생", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error processing unused reservations");
        }
    }
}
