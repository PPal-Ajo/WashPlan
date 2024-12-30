package com.ohgiraffers.washplan.admin.model.service;

import com.ohgiraffers.washplan.admin.model.dao.AdminMapper;
import com.ohgiraffers.washplan.admin.model.dto.AdminDTO;
import com.ohgiraffers.washplan.admin.model.dto.AdminInquiryDTO;
import com.ohgiraffers.washplan.admin.model.dto.AdminInquiryReplyDTO;
import com.ohgiraffers.washplan.admin.model.dto.AdminMachineDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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

    public List<AdminMachineDTO> getWashMachineInfo() {
        return adminMapper.findWashMachineInfo();
    }

    public List<AdminMachineDTO> getDryMachineInfo() {
        return adminMapper.findDryMachineInfo();
    }

    public void changeMachineStatus(List<Integer> machineNos) {
        machineNos.forEach(adminMapper::toggleMachineStatus);
    }

    public List<AdminInquiryDTO> getAllInquiries() {
        return adminMapper.findInquiryInfo();
    }

    public List<AdminInquiryDTO> searchInquiriesByUserIdOrTitle(String query) {
        return adminMapper.findInquiriesByUserIdOrTitle(query);
    }

    public List<AdminInquiryDTO> searchInquiriesByReplyStatus(String status) {
        return adminMapper.findInquiriesByReplyStatus(status);
    }

    public void deleteInquiries(List<Integer> inquiryNos) {
        adminMapper.deleteInquiries(inquiryNos);
    }

    public AdminInquiryDTO findInquiryDetailByNo(int inquiryNo) {
        return adminMapper.findInquiryDetail(inquiryNo);
    }

    public void saveReply(AdminInquiryReplyDTO replyDTO) {
        replyDTO.setAdminNo(2);
        System.out.println("Reply Comment: " + replyDTO.getReplyComment()); // 디버깅용
        adminMapper.insertInquiryReply(replyDTO);
    }

    public String getReplyCommentByInquiryNo(int inquiryNo) {
        return adminMapper.getReplyCommentByInquiryNo(inquiryNo);
    }

    public void updateReplyStatus(int inquiryNo, String replyStatus) {
        adminMapper.updateReplyStatus(inquiryNo, replyStatus);

    }
}
