package com.ohgiraffers.washplan.reservation.model.dto;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.time.LocalDate;
import java.time.LocalTime;

public class ReservationDTO {
    private int machineNo;
    private int userNo;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private LocalDate reserveDate;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "HH:mm")
    private LocalTime StartTime;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "HH:mm")
    private LocalTime EndTime;
    private String reserveOption;
    private int reservePrice;
    private String reserveStatus;

    public ReservationDTO() {}

    public ReservationDTO(int machineNo, int userNo, LocalDate reserveDate, LocalTime startTime, LocalTime endTime, String reserveOption, int reservePrice, String reserveStatus) {
        this.machineNo = machineNo;
        this.userNo = userNo;
        this.reserveDate = reserveDate;
        StartTime = startTime;
        EndTime = endTime;
        this.reserveOption = reserveOption;
        this.reservePrice = reservePrice;
        this.reserveStatus = reserveStatus;
    }

    public int getMachineNo() {
        return machineNo;
    }

    public void setMachineNo(int machineNo) {
        this.machineNo = machineNo;
    }

    public int getUserNo() {
        return userNo;
    }

    public void setUserNo(int userNo) {
        this.userNo = userNo;
    }

    public LocalDate getReserveDate() {
        return reserveDate;
    }

    public void setReserveDate(LocalDate reserveDate) {
        this.reserveDate = reserveDate;
    }

    public LocalTime getStartTime() {
        return StartTime;
    }

    public void setStartTime(LocalTime startTime) {
        StartTime = startTime;
    }

    public LocalTime getEndTime() {
        return EndTime;
    }

    public void setEndTime(LocalTime endTime) {
        EndTime = endTime;
    }

    public String getReserveOption() {
        return reserveOption;
    }

    public void setReserveOption(String reserveOption) {
        this.reserveOption = reserveOption;
    }

    public int getReservePrice() {
        return reservePrice;
    }

    public void setReservePrice(int reservePrice) {
        this.reservePrice = reservePrice;
    }

    public String getReserveStatus() {
        return reserveStatus;
    }

    public void setReserveStatus(String reserveStatus) {
        this.reserveStatus = reserveStatus;
    }

    @Override
    public String toString() {
        return "ReservationDTO{" +
                "machineNo=" + machineNo +
                ", userNo=" + userNo +
                ", reserveDate=" + reserveDate +
                ", StartTime=" + StartTime +
                ", EndTime=" + EndTime +
                ", reserveOption='" + reserveOption + '\'' +
                ", reservePrice=" + reservePrice +
                ", reserveStatus='" + reserveStatus + '\'' +
                '}';
    }
}
