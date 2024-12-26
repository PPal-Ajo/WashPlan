package com.ohgiraffers.washplan.admin.model.service;

import com.ohgiraffers.washplan.admin.model.dao.AdminMapper;
import com.ohgiraffers.washplan.admin.model.dto.AdminDTO;
import com.ohgiraffers.washplan.admin.model.dto.MachineDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class AdminService {
    private final AdminMapper adminMapper;

    @Autowired
    public AdminService(AdminMapper adminMapper) {
        this.adminMapper = adminMapper;
    }

    public List<AdminDTO> getAllUsers() {
       return adminMapper.findUserInfo();

    }


    public void pauseUsers(List<Integer> userNos) {
        for (int userNo : userNos) {
            adminMapper.pauseUser(userNo);
        }
    }

    public void deleteUsers(List<Integer> userNos) {
        for (int userNo : userNos) {
            adminMapper.deleteUser(userNo);
        }
    }

    public List<AdminDTO> searchAll(String keyword) {
        return adminMapper.searchAll("%" + keyword + "%");
    }

    public List<AdminDTO> searchByCancelCount(int cancelCount) {
        return adminMapper.searchByCancelCount(cancelCount);
    }

    public List<MachineDTO> getWashMachineInfo() {
        return adminMapper.findWashMachineInfo();
    }

    public List<MachineDTO> getDryMachineInfo() {
        return adminMapper.findDryMachineInfo();
    }

    public void changeMachineStatus(List<Integer> machineNos) {
        machineNos.forEach(adminMapper::toggleMachineStatus);
    }
}
