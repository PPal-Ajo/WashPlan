package com.ohgiraffers.washplan.reservation.model.dao;

import com.ohgiraffers.washplan.reservation.model.dto.ReservationDTO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

@Mapper
public interface ReservationMapper {


    void insertReservation(ReservationDTO reservationDTO);

    List<Map<String, Object>> findReservations(int machineNo);

    int findMachineNo101();

    int findMachineNo102();

    int findMachineNo103();

    int findMachineNo201();

    int findMachineNo202();

    int findMachineNo203();

    

    void updateReservationStatus();


    int checkReservation(Map<String, Object> params);
}
