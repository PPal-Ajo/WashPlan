package com.ohgiraffers.washplan.reservation.model.dao;

import com.ohgiraffers.washplan.reservation.model.dto.ReservationDTO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

@Mapper
public interface ReservationMapper {
    ReservationDTO findMachineNo();

    void insertReservation(ReservationDTO reservationDTO);

    List<Map<String, Object>> findReservations(int machineNo);
}
