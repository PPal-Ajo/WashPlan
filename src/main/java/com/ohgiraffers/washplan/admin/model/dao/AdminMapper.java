package com.ohgiraffers.washplan.admin.model.dao;

import com.ohgiraffers.washplan.admin.model.dto.*;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.time.LocalDate;
import java.util.Map;

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

    List<AdminInquiryDTO> findInquiriesByUserIdOrTitle(String query);

    List<AdminInquiryDTO> findInquiriesByReplyStatus(String status);

    void deleteInquiries(List<Integer> inquiryNos);

    AdminInquiryDTO findInquiryDetail(int inquiryNo);

    void insertInquiryReply(AdminInquiryReplyDTO replyDTO);

    String getReplyCommentByInquiryNo(int inquiryNo);

    void updateReplyStatus(int inquiryNo, String replyStatus);

    List<AdminNoticeDTO> findNoticeInfo();

    List<AdminNoticeDTO> findNoticesByTitle(String title);

    void deleteNotices(List<Integer> noticeNos);

    void insertNotice(AdminNoticeDTO noticeDTO);

    AdminNoticeDTO findLastAddedNotice(int adminNo);

    AdminNoticeDTO getNoticeById(int noticeNo);

    void updateNotice(AdminNoticeDTO noticeDTO);

    int checkCancelCount(int userNo);

    void insertPenalty(@Param("userNo") int userNo, 
                      @Param("adminNo") int adminNo, 
                      @Param("isPermanent") boolean isPermanent);

    void updateExpiredPenalties();

    String getUserStatus(String userId);

    LocalDate getPenaltyEndDate(String userId);

    int getDailySales(String date);
    
    int getMonthlySales(String yearMonth);
    
    int getPeriodSales(@Param("startDate") String startDate, @Param("endDate") String endDate);
    
    List<Map<String, Object>> getMachineSales(String date);
    
    List<Map<String, Object>> getOptionSales(String date);

    void insertMachine(AdminMachineDTO machine);
}
