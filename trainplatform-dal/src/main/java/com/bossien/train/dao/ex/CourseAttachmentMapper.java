package com.bossien.train.dao.ex;

import com.bossien.train.domain.CourseAttachment;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
public interface CourseAttachmentMapper {

    List<CourseAttachment> selectList(Map<String, Object> params);

    int insertSelective(CourseAttachment courseAttachment);

    int insertBatch(List<CourseAttachment> list);

    int updateByPrimaryKeySelective(CourseAttachment courseAttachment);

    int deleteByPrimaryKey(Map<String, Object> params);

    int selectIntAttachmentIdCount(Map<String, Object> params);

    CourseAttachment selectOne(Map<String, Object> params);

    CourseAttachment selectById(Map<String, Object> params);

}
