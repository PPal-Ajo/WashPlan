package com.ohgiraffers.washplan.user.model.dto;

import java.time.LocalDate;
import java.time.LocalTime;

public class ReservationDetailsDTO {
    private int reserveNo;
    private String machineNo;
    private LocalDate reserveDate;
    private LocalTime startTime;
    private String reserveOption;
    private int reservePrice;

    public ReservationDetailsDTO() {}

    public ReservationDetailsDTO(int reserveNo, String machineNo, LocalDate reserveDate, LocalTime startTime, String reserveOption, int reservePrice) {
        this.reserveNo = reserveNo;
        this.machineNo = machineNo;
        this.reserveDate = reserveDate;
        this.startTime = startTime;
        this.reserveOption = reserveOption;
        this.reservePrice = reservePrice;
    }

    public int getReserveNo() {
        return reserveNo;
    }

    public void setReserveNo(int reserveNo) {
        this.reserveNo = reserveNo;
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
                "reserveNo=" + reserveNo +
                ", machineNo='" + machineNo + '\'' +
                ", reserveDate=" + reserveDate +
                ", startTime=" + startTime +
                ", reserveOption='" + reserveOption + '\'' +
                ", reservePrice=" + reservePrice +
                '}';
    }
}

