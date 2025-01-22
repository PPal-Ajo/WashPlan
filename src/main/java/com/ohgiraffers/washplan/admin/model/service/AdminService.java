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
import java.util.HashMap;
import java.util.ArrayList;
import java.util.stream.Collectors;

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
        return adminMapper.searchAll();
    }

    public List<AdminDTO> searchById(String keyword) {
        return adminMapper.searchById("%" + keyword + "%");
    }

    public List<AdminDTO> searchByStatus(String status) {
        // 한글 상태를 DB 상태값으로 변환
        String dbStatus = switch(status) {
            case "활성" -> "활성";
            case "일시정지" -> "일시정지";
            case "영구탈퇴" -> "영구탈퇴";
            default -> status;
        };
        return adminMapper.searchByStatus(dbStatus);
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

    public List<AdminInquiryDTO> findAllInquiries() {
        return adminMapper.findAllInquiries();
    }

    public List<AdminInquiryDTO> searchInquiriesByUserId(String query) {
        return adminMapper.findInquiriesByUserId(query);
    }

    public List<AdminInquiryDTO> searchInquiriesByTitle(String query) {
        return adminMapper.findInquiriesByTitle(query);
    }

    public List<AdminInquiryDTO> searchInquiriesByReplyStatus(String status) {
        // 한글 상태를 DB 상태값으로 변환
        String dbStatus = switch(status) {
            case "완료" -> "완료";
            case "대기중" -> "대기중";
            default -> status;
        };
        return adminMapper.findInquiriesByReplyStatus(dbStatus);
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
            List<Map<String, Object>> detailSales = adminMapper.getDailyDetailSales(today.toString());
            return formatDetailSales(detailSales, "오늘");
        }
        
        if (message.contains("이번달") && message.contains("매출")) {
            String yearMonth = today.format(DateTimeFormatter.ofPattern("yyyy-MM"));
            List<Map<String, Object>> detailSales = adminMapper.getMonthlyDetailSales(yearMonth);
            String currentMonth = today.format(DateTimeFormatter.ofPattern("M")) + "월";
            return formatDetailSales(detailSales, currentMonth);
        }
        
        if (message.contains("이번년도") && message.contains("매출")) {
            String year = today.format(DateTimeFormatter.ofPattern("yyyy"));
            List<Map<String, Object>> detailSales = adminMapper.getYearlyDetailSales(year);
            if (message.contains("총")) {
                return formatYearlyTotalSales(detailSales, year);
            }
            return formatDetailSales(detailSales, "이번년도");
        }
        
        if (message.contains("작년") || message.contains("2024년") && message.contains("매출")) {
            String year = String.valueOf(today.getYear() - 1);  // 2024
            List<Map<String, Object>> detailSales = adminMapper.getYearlyDetailSales(year);
            if (message.contains("총")) {
                return formatYearlyTotalSales(detailSales, year);
            }
            return formatLastYearDetailSales(detailSales);
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
        
        if (message.contains("회원") || message.contains("사용자")) {
            Map<String, Object> userStats = adminMapper.getUserStats();
            return formatUserStats(userStats);
        }
        
        if (message.contains("기기") && message.contains("현황")) {
            List<Map<String, Object>> machineStatus = adminMapper.getMachineStatus();
            return formatMachineStatus(machineStatus);
        }
        
        if (message.contains("문의") || message.contains("문의사항")) {
            Map<String, Object> inquiryStats = adminMapper.getInquiryStats();
            return formatInquiryStats(inquiryStats);
        }
        
        return "죄송합니다. 이해하지 못했습니다. 다음과 같이 물어보세요:\n" +
               "■ 매출 조회\n" +
               "- 오늘 매출\n" +
               "- 이번달 매출\n" +
               "- 이번년도 매출\n" +
               "- 이번년도 총 매출\n" +
               "- 작년 매출\n" +
               "- 작년 총 매출\n" +
               "■ 현황 조회\n" +
               "- 회원 현황\n" +
               "- 기기 현황\n" +
               "- 문의사항 현황";
    }

    private String formatDetailSales(List<Map<String, Object>> detailSales, String period) {
        if (period.equals("이번년도")) {
            return formatYearlyDetailSales(detailSales);
        }

        Map<String, Map<String, Object>> washingCounts = new HashMap<>();
        Map<String, Map<String, Object>> dryingCounts = new HashMap<>();
        int washingTotal = 0;
        int dryingTotal = 0;
        
        // 데이터 분류
        for (Map<String, Object> sale : detailSales) {
            String machineType = (String) sale.get("MACHINE_TYPE");
            String option = (String) sale.get("RESERVE_OPTION");
            int count = ((Number) sale.get("count")).intValue();
            int total = ((Number) sale.get("total")).intValue();
            int optionPrice = ((Number) sale.get("OPTION_PRICE")).intValue();
            
            Map<String, Object> optionData = new HashMap<>();
            optionData.put("count", count);
            optionData.put("total", total);
            optionData.put("price", optionPrice);
            
            if ("세탁기".equals(machineType)) {
                washingCounts.put(option, optionData);
                washingTotal += total;
            } else if ("건조기".equals(machineType)) {
                dryingCounts.put(option, optionData);
                dryingTotal += total;
            }
        }
        
        StringBuilder response = new StringBuilder(period + " 매출 현황:\n\n");
        
        // 세탁기 정보
        response.append("■ 세탁기\n");
        washingCounts.forEach((option, data) -> {
            response.append(String.format("- %s(%,d원): %d회\n", 
                option, 
                ((Number)data.get("price")).intValue(),
                ((Number)data.get("count")).intValue()));
            response.append(String.format("- %s 총 가격: %,d원\n", 
                option,
                ((Number)data.get("total")).intValue()));
        });
        response.append(String.format("총 사용 횟수: %d회\n", 
            washingCounts.values().stream()
                .mapToInt(data -> ((Number)data.get("count")).intValue())
                .sum()));
        response.append(String.format("세탁기 매출: %,d원\n\n", washingTotal));
        
        // 건조기 정보
        response.append("■ 건조기\n");
        dryingCounts.forEach((option, data) -> {
            response.append(String.format("- %s(%,d원): %d회\n", 
                option, 
                ((Number)data.get("price")).intValue(),
                ((Number)data.get("count")).intValue()));
            response.append(String.format("- %s 총 가격: %,d원\n", 
                option,
                ((Number)data.get("total")).intValue()));
        });
        response.append(String.format("총 사용 횟수: %d회\n", 
            dryingCounts.values().stream()
                .mapToInt(data -> ((Number)data.get("count")).intValue())
                .sum()));
        response.append(String.format("건조기 매출: %,d원\n\n", dryingTotal));
        
        // 전체 매출
        response.append(String.format("▶ %s 전체 매출: %,d원", period, washingTotal + dryingTotal));
        
        return response.toString();
    }

    private String formatYearlyDetailSales(List<Map<String, Object>> detailSales) {
        Map<Integer, List<Map<String, Object>>> salesByMonth = detailSales.stream()
            .collect(Collectors.groupingBy(sale -> ((Number)sale.get("MONTH")).intValue()));
        
        StringBuilder response = new StringBuilder("2025년도 매출 현황:\n\n");
        
        for (int month = 1; month <= 12; month++) {
            List<Map<String, Object>> monthSales = salesByMonth.getOrDefault(month, new ArrayList<>());
            if (!monthSales.isEmpty()) {
                response.append(formatDetailSales(monthSales, month + "월"));
                response.append("\n\n");
            }
        }
        
        return response.toString();
    }

    public void addMachine(AdminMachineDTO machine) {
        adminMapper.insertMachine(machine);
    }

    public void deleteMachines(List<Integer> machineNos) {
        for (Integer machineNo : machineNos) {
            adminMapper.deleteMachine(machineNo);
        }
    }

    private String formatUserStats(Map<String, Object> stats) {
        StringBuilder response = new StringBuilder("회원 현황:\n\n");
        response.append(String.format("▶ 전체 회원 수: %d명\n", 
            ((Number)stats.get("total_users")).intValue()));
        response.append(String.format("▶ 오늘 신규 가입: %d명", 
            ((Number)stats.get("new_users")).intValue()));
        return response.toString();
    }

    private String formatMachineStatus(List<Map<String, Object>> machines) {
        StringBuilder response = new StringBuilder("기기 현황:\n\n");
        
        // 세탁기 현황
        response.append("■ 세탁기\n");
        machines.stream()
            .filter(m -> "세탁기".equals(m.get("MACHINE_TYPE")))
            .forEach(m -> response.append(String.format("- %d번 기기: %s\n", 
                ((Number)m.get("MACHINE_NO")).intValue(),
                m.get("MACHINE_STATUS"))));
        
        response.append("\n■ 건조기\n");
        machines.stream()
            .filter(m -> "건조기".equals(m.get("MACHINE_TYPE")))
            .forEach(m -> response.append(String.format("- %d번 기기: %s\n", 
                ((Number)m.get("MACHINE_NO")).intValue(),
                m.get("MACHINE_STATUS"))));
        
        return response.toString();
    }

    private String formatInquiryStats(Map<String, Object> stats) {
        StringBuilder response = new StringBuilder("문의사항 현황:\n\n");
        response.append(String.format("▶ 오늘 신규 문의: %d건\n", 
            ((Number)stats.get("today_inquiries")).intValue()));
        response.append(String.format("▶ 답변 대기 문의: %d건", 
            ((Number)stats.get("pending_inquiries")).intValue()));
        return response.toString();
    }

    private String formatYearlyTotalSales(List<Map<String, Object>> detailSales, String year) {
        Map<Integer, Integer> monthlyTotals = new HashMap<>();
        
        for (Map<String, Object> sale : detailSales) {
            int month = ((Number)sale.get("MONTH")).intValue();
            int total = ((Number)sale.get("total")).intValue();
            monthlyTotals.merge(month, total, Integer::sum);
        }
        
        StringBuilder response = new StringBuilder(year + "년 월별 매출:\n\n");
        int yearTotal = 0;
        
        for (int month = 1; month <= 12; month++) {
            int monthTotal = monthlyTotals.getOrDefault(month, 0);
            yearTotal += monthTotal;
            response.append(String.format("%2d월 매출: %,d원\n", month, monthTotal));
        }
        
        response.append(String.format("\n▶ %s년 전체 매출: %,d원", year, yearTotal));
        
        return response.toString();
    }

    private String formatLastYearDetailSales(List<Map<String, Object>> detailSales) {
        Map<Integer, List<Map<String, Object>>> salesByMonth = detailSales.stream()
            .collect(Collectors.groupingBy(sale -> ((Number)sale.get("MONTH")).intValue()));
        
        StringBuilder response = new StringBuilder("2024년도 매출 현황:\n\n");
        
        for (int month = 1; month <= 12; month++) {
            List<Map<String, Object>> monthSales = salesByMonth.getOrDefault(month, new ArrayList<>());
            if (!monthSales.isEmpty()) {
                response.append(formatDetailSales(monthSales, month + "월"));
                response.append("\n\n");
            }
        }
        
        return response.toString();
    }


    public void activeUsers(List<Integer> userNos) {
        if (userNos == null || userNos.isEmpty()) {
            throw new IllegalArgumentException("활성화할 사용자가 선택되지 않았습니다.");
        }
        
        for (Integer userNo : userNos) {
            adminMapper.activeUser(userNo);
        }
    }
}
