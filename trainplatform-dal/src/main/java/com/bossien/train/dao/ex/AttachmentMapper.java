package com.bossien.train.dao.ex;

import com.bossien.train.domain.Attachment;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
public interface AttachmentMapper {

    public List<Attachment> selectByIds(Map<String, Object> params);

    int insertSelective(Attachment attachment);

    int insertBatch(List<Attachment> list);

    int updateBatch(List<Attachment> list);

    int updateByPrimaryKeySelective(Attachment attachment);

    int deleteByPrimaryKey(Map<String, Object> params);

    int deleteByBatch(Map<String, Object> params);

    Attachment selectOne(Map<String, Object> params);

    Attachment selectById(Map<String, Object> params);
}
