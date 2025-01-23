package com.ohgiraffers.washplan.reservation.model.dao;

import com.ohgiraffers.washplan.reservation.model.dto.ReservationDTO;
import com.ohgiraffers.washplan.machine.model.dto.MachineDTO;
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



    void updateReservationStatus();



    int checkReservation(Map<String, Object> params);

    String findMachineStatus101();

    String findMachineStatus102();

    String findMachineStatus103();

    String getMachineStatus(int machineNo);

    List<ReservationDTO> findReservationsByUsername(@Param("username") String username);

    List<ReservationDTO> findReservationsByUser(String username);


    void updateQRCode(ReservationDTO reservationDTO);



    void insertReservationHistory();

    void deleteCompletedReservations();

    List<ReservationDTO> getCurrentReservations();

    List<MachineDTO> findMachinesByType(String machineType);

    ReservationDTO selectReservationByNo(int reserveNo);

    int updateReservationStatusToUsing(int reserveNo);

    ReservationDTO getCurrentReservation(@Param("machineNo") int machineNo, 
                                       @Param("selectedDate") String selectedDate,
                                       @Param("selectedTime") String selectedTime);

    List<Integer> findUnusedReservationUserNos();

    void deleteUnusedReservation();


    void updateUserCancelCount();
}
