package com.ohgiraffers.washplan.user.model.dto;

import java.time.LocalDate;
import java.time.LocalTime;

public class ReservationDetailsDTO {
    private String machineNo;
    private LocalDate reserveDate;
    private LocalTime startTime;
    private String reserveOption;
    private int reservePrice;

    ReservationDetailsDTO () {}

    public ReservationDetailsDTO(String machineNo, LocalDate reserveDate, LocalTime startTime, String reserveOption, int reservePrice) {
        this.machineNo = machineNo;
        this.reserveDate = reserveDate;
        this.startTime = startTime;
        this.reserveOption = reserveOption;
        this.reservePrice = reservePrice;
    }

    public String getMachineNo() {
        return machineNo;
    }

    public void setMachineNo(String machineNo) {
        this.machineNo = machineNo;
    }

    public LocalDate getReserveDate() {
        return reserveDate;
    }

    public void setReserveDate(LocalDate reserveDate) {
        this.reserveDate = reserveDate;
    }

    public LocalTime getStartTime() {
        return startTime;
    }

    public void setStartTime(LocalTime startTime) {
        this.startTime = startTime;
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

    @Override
    public String toString() {
        return "ReservationDetailsDTO{" +
                "machineNo='" + machineNo + '\'' +
                ", reserveDate=" + reserveDate +
                ", startTime=" + startTime +
                ", reserveOption='" + reserveOption + '\'' +
                ", reservePrice=" + reservePrice +
                '}';
    }
}

