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

    void deleteMachine(int machineNo);

    List<Map<String, Object>> getDailyDetailSales(String date);
    List<Map<String, Object>> getMonthlyDetailSales(String yearMonth);
    List<Map<String, Object>> getYearlyDetailSales(String year);

    List<Map<String, Object>> getMonthlyDataForYear(String year);
    Map<String, Object> getUserStats();
    
    List<Map<String, Object>> getMachineStatus();
    
    Map<String, Object> getInquiryStats();

    List<AdminDTO> searchAll();

    List<AdminDTO> searchById(String s);

    List<AdminDTO> searchByStatus(String dbStatus);



    List<AdminInquiryDTO> findInquiriesByUserId(String query);

    List<AdminInquiryDTO> findInquiriesByTitle(String query);

    List<AdminInquiryDTO> findAllInquiries();


    void activeUser(Integer userNo);
}
