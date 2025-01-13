package com.ohgiraffers.washplan.admin.model.service;

import com.ohgiraffers.washplan.admin.model.dao.AdminMapper;
import com.ohgiraffers.washplan.admin.model.dto.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

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
            adminMapper.insertPenalty(userNo, 1, false);
        }
    }

    public void deleteUsers(List<Integer> userNos) {
        for (int userNo : userNos) {
            adminMapper.deleteUser(userNo);
            adminMapper.insertPenalty(userNo, 1, true);
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

    public List<AdminNoticeDTO> getAllNotices() {
        return adminMapper.findNoticeInfo();
    }

    public List<AdminNoticeDTO> searchNoticesByTitle(String title) {
        return adminMapper.findNoticesByTitle(title);
    }

    public void deleteNotices(List<Integer> noticeNos) {
        if (noticeNos != null && !noticeNos.isEmpty()) {
            adminMapper.deleteNotices(noticeNos);
        }
    }

    public void insertNotice(AdminNoticeDTO noticeDTO) {
        adminMapper.insertNotice(noticeDTO);

    }

    public AdminNoticeDTO findLastAddedNotice(AdminNoticeDTO noticeDTO) {
        return adminMapper.findLastAddedNotice(noticeDTO.getAdminNo());
    }



    public void updateNotice(AdminNoticeDTO noticeDTO) {
        adminMapper.updateNotice(noticeDTO);
    }

    public AdminNoticeDTO getNoticeById(int noticeNo) {
        return adminMapper.getNoticeById(noticeNo);
    }

    @Scheduled(cron = "0 0 0 * * *")
    public void checkExpiredPenalties() {
        adminMapper.updateExpiredPenalties();
    }

    public String processMessage(String message) {
        LocalDate today = LocalDate.now();
        
        if (message.contains("오늘") && message.contains("매출")) {
            int sales = adminMapper.getDailySales(today.toString());
            return String.format("오늘의 총 매출은 %,d원입니다.", sales);
        }
        
        if (message.contains("이번달") && message.contains("매출")) {
            String yearMonth = today.format(DateTimeFormatter.ofPattern("yyyy-MM"));
            int sales = adminMapper.getMonthlySales(yearMonth);
            return String.format("이번 달의 총 매출은 %,d원입니다.", sales);
        }
        
        if (message.contains("기기별") || message.contains("기계별")) {
            List<Map<String, Object>> machineSales = adminMapper.getMachineSales(today.toString());
            StringBuilder response = new StringBuilder("오늘의 기기별 매출:\n");
            for (Map<String, Object> sale : machineSales) {
                response.append(String.format("%s: %,d원 (%d건)\n", 
                    sale.get("MACHINE_TYPE"), 
                    ((Number)sale.get("total")).intValue(),
                    ((Number)sale.get("count")).intValue()));
            }
            return response.toString();
        }
        
        if (message.contains("옵션별")) {
            List<Map<String, Object>> optionSales = adminMapper.getOptionSales(today.toString());
            StringBuilder response = new StringBuilder("오늘의 옵션별 매출:\n");
            for (Map<String, Object> sale : optionSales) {
                response.append(String.format("%s: %,d원 (%d건)\n", 
                    sale.get("RESERVE_OPTION"), 
                    ((Number)sale.get("total")).intValue(),
                    ((Number)sale.get("count")).intValue()));
            }
            return response.toString();
        }
        
        return "죄송합니다. 이해하지 못했습니다. '오늘 매출', '이번달 매출', '기기별 매출', '옵션별 매출' 등으로 물어봐주세요.";
    }
}
