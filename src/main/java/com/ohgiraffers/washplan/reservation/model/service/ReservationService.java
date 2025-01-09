package com.ohgiraffers.washplan.reservation.model.service;

import com.ohgiraffers.washplan.reservation.model.dao.ReservationMapper;
import com.ohgiraffers.washplan.reservation.model.dto.ReservationDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class ReservationService {

    private final ReservationMapper reservationMapper;
    @Autowired
    public ReservationService(ReservationMapper reservationMapper) {
        this.reservationMapper = reservationMapper;
    }


    public void saveReservation(ReservationDTO reservationDTO) {
        reservationMapper.insertReservation(reservationDTO);
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

}
