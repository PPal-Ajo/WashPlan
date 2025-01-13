package com.ohgiraffers.washplan.reservation.model.dao;

import com.ohgiraffers.washplan.reservation.model.dto.ReservationDTO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

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



    int updateReservationStatus();



    int checkReservation(Map<String, Object> params);

    String findMachineStatus101();

    String findMachineStatus102();

    String findMachineStatus103();

    String getMachineStatus(int machineNo);

    List<ReservationDTO> findReservationsByUsername(@Param("username") String username);

    List<ReservationDTO> findReservationsByUser(String username);


    void updateQRCode(ReservationDTO reservationDTO);



    int insertReservationHistory();

    int deleteCompletedReservations();

    List<ReservationDTO> getCurrentReservations();
}
