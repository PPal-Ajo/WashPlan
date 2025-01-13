package com.ohgiraffers.washplan.machine.model.dto;

import java.util.List;

public class MachineDTO {
    private int machineNo;
    private String machineType;
    private String machineStatus;
    private List<MachineOptionDTO> options;

    public MachineDTO() {}

    public MachineDTO(int machineNo, String machineType, String machineStatus, List<MachineOptionDTO> options) {
        this.machineNo = machineNo;
        this.machineType = machineType;
        this.machineStatus = machineStatus;
        this.options = options;
    }

    // 상태 업데이트용 생성자
    public MachineDTO(int machineNo, String machineStatus) {
        this.machineNo = machineNo;
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

    public List<MachineOptionDTO> getOptions() {
        return options;
    }

    public void setOptions(List<MachineOptionDTO> options) {
        this.options = options;
    }

    @Override
    public String toString() {
        return "MachineDTO{" +
                "machineNo=" + machineNo +
                ", machineType='" + machineType + '\'' +
                ", machineStatus='" + machineStatus + '\'' +
                ", options=" + options +
                '}';
    }
}
