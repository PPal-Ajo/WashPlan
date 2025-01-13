package com.ohgiraffers.washplan.machine.model.dto;

public class MachineOptionDTO {
    private int optionNo;
    private String machineType;
    private String optionName;
    private int optionPrice;
    private int optionTime;

    public MachineOptionDTO() {};

    public MachineOptionDTO(int optionNo, String machineType, String optionName, int optionPrice, int optionTime) {
        this.optionNo = optionNo;
        this.machineType = machineType;
        this.optionName = optionName;
        this.optionPrice = optionPrice;
        this.optionTime = optionTime;
    }

    public int getOptionNo() {
        return optionNo;
    }

    public void setOptionNo(int optionNo) {
        this.optionNo = optionNo;
    }

    public String getMachineType() {
        return machineType;
    }

    public void setMachineType(String machineType) {
        this.machineType = machineType;
    }

    public String getOptionName() {
        return optionName;
    }

    public void setOptionName(String optionName) {
        this.optionName = optionName;
    }

    public int getOptionPrice() {
        return optionPrice;
    }

    public void setOptionPrice(int optionPrice) {
        this.optionPrice = optionPrice;
    }

    public int getOptionTime() {
        return optionTime;
    }

    public void setOptionTime(int optionTime) {
        this.optionTime = optionTime;
    }

    @Override
    public String toString() {
        return "MachineOptionDTO{" +
                "optionNo=" + optionNo +
                ", machineType='" + machineType + '\'' +
                ", optionName='" + optionName + '\'' +
                ", optionPrice=" + optionPrice +
                ", optionTime=" + optionTime +
                '}';
    }
}



