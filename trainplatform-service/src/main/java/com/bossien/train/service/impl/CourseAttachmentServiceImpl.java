package com.bossien.train.service.impl;

import com.bossien.train.dao.ex.AttachmentMapper;
import com.bossien.train.dao.ex.CourseAttachmentMapper;
import com.bossien.train.domain.Attachment;
import com.bossien.train.domain.CourseAttachment;
import com.bossien.train.domain.dto.CourseAttachmentDTO;
import com.bossien.train.domain.dto.CourseAttachmentMessage;
import com.bossien.train.service.ICourseAttachmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class CourseAttachmentServiceImpl implements ICourseAttachmentService {

    @Autowired
    private CourseAttachmentMapper courseAttachmentMapper;

    @Override
    public List<CourseAttachment> selectList(Map<String, Object> params) {
        return courseAttachmentMapper.selectList(params);
    }

    @Override
    public int insertSelective(CourseAttachmentMessage courseAttachmentMessage) {
        CourseAttachment courseAttachment = new CourseAttachment();
        courseAttachment.setIntCourseId(courseAttachmentMessage.getCourseId());
        courseAttachment.setIntAttachmentId(courseAttachmentMessage.getCourseAttachmentId());
        courseAttachmentMapper.insertSelective(courseAttachment);
        return 0;
    }

    @Override
    public int insertBatch(List<CourseAttachment> list) {
        return courseAttachmentMapper.insertBatch(list);
    }

    @Override
    public int updateByPrimaryKeySelective(CourseAttachmentMessage courseAttachmentMessage) {
        CourseAttachment courseAttachment = new CourseAttachment();
        courseAttachment.setIntCourseId(courseAttachmentMessage.getCourseId());
        courseAttachment.setIntAttachmentId(courseAttachmentMessage.getCourseAttachmentId());
        courseAttachmentMapper.updateByPrimaryKeySelective(courseAttachment);
        return 0;
    }

    @Override
    public int deleteByPrimaryKey(Map<String, Object> params) {
        return courseAttachmentMapper.deleteByPrimaryKey(params);
    }

    @Override
    public int selectIntAttachmentIdCount(Map<String, Object> params) {
        return courseAttachmentMapper.selectIntAttachmentIdCount(params);
    }

    @Override
    public CourseAttachment selectOne(Map<String, Object> params) {
        return courseAttachmentMapper.selectOne(params);
    }

    @Override
    public CourseAttachment selectById(Map<String, Object> params) {
        return courseAttachmentMapper.selectById(params);
    }
}
