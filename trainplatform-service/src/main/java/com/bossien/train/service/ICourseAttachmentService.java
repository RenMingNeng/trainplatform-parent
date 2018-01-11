package com.bossien.train.service;

import com.bossien.train.domain.CourseAttachment;
import com.bossien.train.domain.dto.CourseAttachmentDTO;
import com.bossien.train.domain.dto.CourseAttachmentMessage;

import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2017/7/26.
 */
public interface ICourseAttachmentService {

    List<CourseAttachment> selectList(Map<String, Object> params);

    int insertSelective(CourseAttachmentMessage courseAttachmentMessage);

    int insertBatch(List<CourseAttachment> list);

    int updateByPrimaryKeySelective(CourseAttachmentMessage courseAttachmentMessage);

    int deleteByPrimaryKey(Map<String, Object> params);

    int selectIntAttachmentIdCount(Map<String, Object> params);

    CourseAttachment selectOne(Map<String, Object> params);

    CourseAttachment selectById(Map<String, Object> params);
}
