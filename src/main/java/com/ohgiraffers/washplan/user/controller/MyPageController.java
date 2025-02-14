package com.ohgiraffers.washplan.user.controller;

import com.ohgiraffers.washplan.auth.model.dto.CustomUserDetails;
import com.ohgiraffers.washplan.reservation.model.dto.ReservationDTO;
import com.ohgiraffers.washplan.reservation.model.service.ReservationService;
import com.ohgiraffers.washplan.user.model.dto.ReservationDetailsDTO;
import com.ohgiraffers.washplan.user.model.service.MyPageService;
import com.ohgiraffers.washplan.user.model.service.UserService;
import com.ohgiraffers.washplan.reservation.model.service.QRCodeService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Base64;

@Controller
public class MyPageController {

    private final UserService userService;
    private final MyPageService myPageService;
    private final ReservationService reservationService;
    private final QRCodeService qrCodeService;

    public MyPageController(UserService userService, MyPageService myPageService, ReservationService reservationService, QRCodeService qrCodeService) {
        this.userService = userService;
        this.myPageService = myPageService;
        this.reservationService = reservationService;
        this.qrCodeService = qrCodeService;
    }

    @GetMapping("/mypage")
    public String myPage(@AuthenticationPrincipal CustomUserDetails userDetails, Model model) {

        model.addAttribute("username", userDetails.getUsername());
        model.addAttribute("email", userDetails.getEmail());

        return "mypage/mypage";
    }

    @PostMapping("/change-password")
    public ResponseEntity<Map<String, Object>> changePassword(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @RequestBody Map<String, String> request) {
        Map<String, Object> response = new HashMap<>();
        String email = userDetails.getEmail();
        String userId = userDetails.getUsername();
        String newPassword = request.get("password");

        if (newPassword == null || newPassword.isBlank()) {
            response.put("success", false);
            response.put("message", "비밀번호는 필수입니다.");
            return ResponseEntity.badRequest().body(response);
        }

        if (newPassword.length() < 8 || !newPassword.matches(".*\\d.*") || !newPassword.matches(".*[!@#$%^&*].*")) {
            response.put("success", false);
            response.put("message", "비밀번호는 8자리 이상이어야 하며 숫자와 특수문자를 포함해야 합니다.");
            return ResponseEntity.badRequest().body(response);
        }

        boolean isUpdated = userService.updatePassword(email, userId, newPassword);

        if (isUpdated) {
            response.put("success", true);
            response.put("message", "비밀번호가 변경되었습니다.");
            return ResponseEntity.ok(response);
        } else {
            response.put("success", false);
            response.put("message", "비밀번호 변경에 실패했습니다.");
            return ResponseEntity.status(500).body(response);
        }
    }

    @PostMapping("/delete-account")
    public ResponseEntity<String> deleteAccount(@AuthenticationPrincipal CustomUserDetails userDetails) {
        try {
            String userId = userDetails.getUsername();
           myPageService.deleteUserAndRelatedData(userId);
            return ResponseEntity.ok("회원 탈퇴가 완료되었습니다.");
        } catch (Exception e) {
            return ResponseEntity.status(500).body("회원 탈퇴 중 문제가 발생했습니다.");
        }
    }

    @GetMapping("/mypage/reservations")
    public String reservationsPage(@AuthenticationPrincipal CustomUserDetails userDetails, Model model) {
        int userNo = userDetails.getUserNo();
        List<ReservationDetailsDTO> reservations = myPageService.getUserReservations(userNo);
        
        // 예약 정보를 Model에 추가
        model.addAttribute("reservations", reservations);
        
        return "mypage/reservation";
    }

    @PostMapping("/mypage/reservations/delete")
    public ResponseEntity<Map<String, String>> deleteReservation(@RequestBody Map<String, Object> request) {
        Map<String, String> response = new HashMap<>();
        try {
            // reserveNo를 String에서 Integer로 변환
            Integer reserveNo = Integer.parseInt(request.get("reserveNo").toString());

            // 예약 삭제 서비스 호출
            boolean isDeleted = myPageService.deleteReservation(reserveNo);

            if (isDeleted) {
                response.put("success", "true");
                response.put("message", "예약이 성공적으로 삭제되었습니다.");
                return ResponseEntity.ok(response);
            } else {
                response.put("success", "false");
                response.put("message", "예약 삭제에 실패했습니다.");
                return ResponseEntity.badRequest().body(response);
            }
        } catch (Exception e) {
            response.put("success", "false");
            response.put("message", "서버 오류가 발생했습니다.");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    @GetMapping("/mypage/reservations/qr/{reserveNo}")
    @ResponseBody
    public ResponseEntity<?> getQRCode(@PathVariable int reserveNo) {
        try {
            byte[] qrCode = qrCodeService.generateMyPageQRCode(String.valueOf(reserveNo), 200, 200);
            return ResponseEntity.ok(Map.of("qrCode", Base64.getEncoder().encodeToString(qrCode)));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    @GetMapping("/mypage/reservations/scan/{reserveNo}")
    public String handleQRScanFromMobile(@PathVariable int reserveNo, RedirectAttributes redirectAttributes) {
        try {
            // 예약 정보 조회
            ReservationDTO reservation = reservationService.findReservationByNo(reserveNo);
            
            if (reservation == null) {
                redirectAttributes.addFlashAttribute("error", "유효하지 않은 예약입니다.");
                return "redirect:/reservation/error";
            }
            
            if (!"예약중".equals(reservation.getReserveStatus())) {
                redirectAttributes.addFlashAttribute("error", 
                    "이미 처리된 예약이거나 취소된 예약입니다. (현재 상태: " + reservation.getReserveStatus() + ")");
                return "redirect:/reservation/error";
            }
            
            // 단순히 DB의 예약 상태만 '사용중'으로 업데이트
            reservationService.updateReservationStatusToUsing(reserveNo);
            
            redirectAttributes.addFlashAttribute("success", "QR 코드 스캔이 완료되었습니다. 이용을 시작하세요.");
            return "redirect:/reservation/success";
            
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "처리 중 오류가 발생했습니다: " + e.getMessage());
            return "redirect:/reservation/error";
        }
    }

    @GetMapping("/mypage/reservations/status/{reserveNo}")
    @ResponseBody
    public String getReservationStatus(@PathVariable int reserveNo) {
        ReservationDTO reservation = reservationService.findReservationByNo(reserveNo);
        return reservation.getReserveStatus();
    }

}
