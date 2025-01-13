package com.ohgiraffers.washplan.machine.model.dao;

import com.ohgiraffers.washplan.machine.model.dto.MachineDTO;
import com.ohgiraffers.washplan.machine.model.dto.MachineOptionDTO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface MachineMapper {
    List<MachineDTO> selectMachinesByType(String machineType);
    List<MachineOptionDTO> selectOptionsByType(String machineType);
    void updateMachineStatus(MachineDTO machine);
    MachineDTO selectMachineByNo(int machineNo);
    String selectMachineStatus(int machineNo);
}
