package com.bossien.train.service;

import com.bossien.train.domain.SubjectCourse;
import java.util.List;
import java.util.Map;

public interface ISubjectCourseService {

    int deleteByPrimaryKey(String intId);

    int deleteByTrainSubjectId(String trainSubjectId);

    int insert(SubjectCourse record);

    int insertSelective(SubjectCourse record);

    SubjectCourse selectByPrimaryKey(String intId);

    int updateByPrimaryKeySelective(SubjectCourse record);

    int updateByPrimaryKey(SubjectCourse record);
    int deleteByCourseId(Map<String, Object> map);
    /**
     * 根据主题Id拿到课程Id集合
     * @param subjectId
     * @return
     */
    List<String> selectCourseIds(String subjectId);

    /**
     * 批量添加培训主题和课程的关联关系
     * @param params
     * @return
     */
    int saveTrainSubjectCourse(Map<String,Object> params);
}
