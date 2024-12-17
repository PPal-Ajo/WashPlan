package com.ohgiraffers.washplan.reservation.controller;

import com.ohgiraffers.washplan.reservation.model.dto.ReservationDTO;
import com.ohgiraffers.washplan.reservation.model.service.ReservationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
public class ReservationController {

    private final ReservationService reservationService;

    @Autowired
    public ReservationController(ReservationService reservationService) {
        this.reservationService = reservationService;
    }

    @GetMapping("/reservation")
    public String reservation(Model model) {
        // 기기 번호 조회
        ReservationDTO reservationDTO = reservationService.getMachineNo();

        // 모델에 기기 번호 추가
        model.addAttribute("machineNo", reservationDTO.getMachineNo());


        return "reservation/reservation";
    }
    @PostMapping("/reservation/save")
    public ResponseEntity<String> saveReservation(@RequestBody ReservationDTO reservationDTO) {
        // 유저 번호 임시 설정
        reservationDTO.setUserNo(1);

        // 예약 정보 저장
        reservationService.saveReservation(reservationDTO);

        return new ResponseEntity<>("Reservation saved successfully!", HttpStatus.OK);
    }

    @GetMapping("/reservation/status")
    public ResponseEntity<List<Map<String, Object>>> getReservationStatus(@RequestParam("machineNo") int machineNo) {
        List<Map<String, Object>> reservations = reservationService.getReservations(machineNo);
        return ResponseEntity.ok(reservations);
    }






}
