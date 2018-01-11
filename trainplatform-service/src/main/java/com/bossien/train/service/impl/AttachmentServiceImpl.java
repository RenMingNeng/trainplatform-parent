package com.bossien.train.service.impl;

import com.bossien.train.dao.ex.AttachmentMapper;
import com.bossien.train.domain.Attachment;
import com.bossien.train.domain.dto.AttachmentDTO;
import com.bossien.train.domain.dto.AttachmentMessage;
import com.bossien.train.service.IAttachmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

@Service
public class AttachmentServiceImpl implements IAttachmentService {

    @Autowired
    private AttachmentMapper attachmentMapper;

    @Override
    public List<Attachment> selectByIds(Map<String, Object> params) {
        return attachmentMapper.selectByIds(params);
    }

    @Override
    public int insertSelective(AttachmentMessage attachmentMessage) {
        Attachment attachment = new Attachment();
        attachment.setIntId(attachmentMessage.getAttachmentId());
        attachment.setVarFid(attachmentMessage.getAttachmentFid());
        attachment.setVarLocalName(attachmentMessage.getAttachmentLocalName());
        attachment.setVarRemotePath(attachmentMessage.getAttachmentRemotePath());
        attachment.setVarExt(attachmentMessage.getAttachmentExt());
        attachment.setVarType(attachmentMessage.getAttachmentType());
        attachment.setIntBusinessId(attachmentMessage.getAttachmentBusinessId());
        attachment.setVarMd5(attachmentMessage.getAttachmentMd5());
        attachment.setChrComplete(attachmentMessage.getAttachmentStatus());
        attachment.setDatCreateDate(attachmentMessage.getDatCreateDate());
        attachment.setDatOperDate(attachmentMessage.getDatOperDate());
        attachment.setVarCreateUser(attachmentMessage.getCreateUser());
        attachment.setVarOperUser(attachmentMessage.getOperUser());
      /*  SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        attachment.setDatOperDate(dateFormat.format(new Date()));
        attachment.setDatCreateDate(dateFormat.format(new Date()));*/
        attachmentMapper.insertSelective(attachment);
        return 0;
    }

    @Override
    public int update(AttachmentMessage attachmentMessage) {
        Attachment attachment = new Attachment();
        attachment.setIntId(attachmentMessage.getAttachmentId());
        attachment.setVarFid(attachmentMessage.getAttachmentFid());
        attachment.setVarLocalName(attachmentMessage.getAttachmentLocalName());
        attachment.setVarRemotePath(attachmentMessage.getAttachmentRemotePath());
        attachment.setVarExt(attachmentMessage.getAttachmentExt());
        attachment.setVarType(attachmentMessage.getAttachmentType());
        attachment.setIntBusinessId(attachmentMessage.getAttachmentBusinessId());
        attachment.setVarMd5(attachmentMessage.getAttachmentMd5());
        attachment.setChrComplete(attachmentMessage.getAttachmentStatus());
        attachment.setDatCreateDate(attachmentMessage.getDatCreateDate());
        attachment.setDatOperDate(attachmentMessage.getDatOperDate());
        attachment.setVarCreateUser(attachmentMessage.getCreateUser());
        attachment.setVarOperUser(attachmentMessage.getOperUser());
       /* SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        attachment.setDatOperDate(dateFormat.format(new Date()));
        attachment.setDatCreateDate(dateFormat.format(new Date()));*/
        attachmentMapper.updateByPrimaryKeySelective(attachment);

        return 0;
    }

    @Override
    public Attachment selectOne(Map<String, Object> params) {
        return attachmentMapper.selectOne(params);
    }

    @Override
    public Attachment selectById(Map<String, Object> params) {
        return attachmentMapper.selectById(params);
    }

    @Override
    public int deleteByPrimaryKey(Map<String, Object> params) {
        return attachmentMapper.deleteByPrimaryKey(params);
    }

    @Override
    public int insertBatch(List<Attachment> list) {
        return attachmentMapper.insertBatch(list);
    }

    @Override
    public int updateBatch(List<Attachment> list) {
        return attachmentMapper.updateBatch(list);
    }
}
