package com.ohgiraffers.washplan.notice.model.dao;

import com.ohgiraffers.washplan.notice.model.dto.NoticeDTO;
import org.apache.ibatis.annotations.Mapper;
import java.util.List;

@Mapper
public interface NoticeMapper {
    List<NoticeDTO> findAllNotices();
}
