package com.ohgiraffers.washplan.admin.model.dao;

import com.ohgiraffers.washplan.admin.model.dto.AdminDTO;
import com.ohgiraffers.washplan.admin.model.dto.AdminInquiryDTO;
import com.ohgiraffers.washplan.admin.model.dto.AdminMachineDTO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface AdminMapper {
    List<AdminDTO> findUserInfo();
    

    void pauseUser(int userNo);

    void deleteUser(int userNo);

    List<AdminDTO> searchAll(String s);

    List<AdminDTO> searchByCancelCount(int cancelCount);

    List<AdminMachineDTO> findWashMachineInfo();

    List<AdminMachineDTO> findDryMachineInfo();

    void toggleMachineStatus(Integer integer);

    List<AdminInquiryDTO> findInquiryInfo();
}
