package com.bossien.train.service;

import com.bossien.train.domain.Attachment;
import com.bossien.train.domain.CourseAttachment;
import com.bossien.train.domain.dto.AttachmentDTO;
import com.bossien.train.domain.dto.AttachmentMessage;

import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2017/7/26.
 */
public interface IAttachmentService {

     List<Attachment> selectByIds(Map<String, Object> params);

    int insertSelective(AttachmentMessage attachmentMessage);

    int update(AttachmentMessage attachmentMessage);

    Attachment selectOne(Map<String, Object> params);

    Attachment selectById(Map<String, Object> params);

    int deleteByPrimaryKey(Map<String, Object> params);

    int insertBatch(List<Attachment> list);

    int updateBatch(List<Attachment> list);
}
