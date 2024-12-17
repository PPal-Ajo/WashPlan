package com.ohgiraffers.washplan.reservation.model.service;

import com.ohgiraffers.washplan.reservation.model.dao.ReservationMapper;
import com.ohgiraffers.washplan.reservation.model.dto.ReservationDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

@Service
public class ReservationService {

    private final ReservationMapper reservationMapper;
    @Autowired
    public ReservationService(ReservationMapper reservationMapper) {
        this.reservationMapper = reservationMapper;
    }
    public ReservationDTO getMachineNo() {
        return reservationMapper.findMachineNo();
    }


    public void saveReservation(ReservationDTO reservationDTO) {
        reservationMapper.insertReservation(reservationDTO);
    }

    public List<Map<String, Object>> getReservations(int machineNo) {
        return reservationMapper.findReservations(machineNo);
    }
}
