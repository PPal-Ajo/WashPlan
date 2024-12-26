package com.ohgiraffers.washplan.admin.model.dto;

public class MachineDTO {
    private int machineNo;
    private String machineType;
    private String machineStatus;

    public MachineDTO() {};

    public MachineDTO(int machineNo, String machineType, String machineStatus) {
        this.machineNo = machineNo;
        this.machineType = machineType;
        this.machineStatus = machineStatus;
    }

    public int getMachineNo() {
        return machineNo;
    }

    public void setMachineNo(int machineNo) {
        this.machineNo = machineNo;
    }

    public String getMachineType() {
        return machineType;
    }

    public void setMachineType(String machineType) {
        this.machineType = machineType;
    }

    public String getMachineStatus() {
        return machineStatus;
    }

    public void setMachineStatus(String machineStatus) {
        this.machineStatus = machineStatus;
    }

    @Override
    public String toString() {
        return "MachineDTO{" +
                "machineNo=" + machineNo +
                ", machineType='" + machineType + '\'' +
                ", machineStatus='" + machineStatus + '\'' +
                '}';
    }
}

