package com.ohgiraffers.washplan.machine.model.service;

import com.ohgiraffers.washplan.machine.model.dao.MachineMapper;
import com.ohgiraffers.washplan.machine.model.dto.MachineDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class MachineService {
    
    private final MachineMapper machineMapper;
    
    @Autowired
    public MachineService(MachineMapper machineMapper) {
        this.machineMapper = machineMapper;
    }
    
    public List<MachineDTO> getMachinesByType(String machineType) {
        return machineMapper.selectMachinesByType(machineType);
    }
    
    public void updateMachineStatus(int machineNo, String status) {
        MachineDTO machine = new MachineDTO(machineNo, status);
        machineMapper.updateMachineStatus(machine);
    }
    
    public MachineDTO getMachineByNo(int machineNo) {
        return machineMapper.selectMachineByNo(machineNo);
    }
    
    public String getMachineStatus(int machineNo) {
        return machineMapper.selectMachineStatus(machineNo);
    }
}
